package com.github.unchama.seichiassist.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import com.github.unchama.seichiassist.SeichiAssist;
import com.github.unchama.seichiassist.Sql;
import com.github.unchama.seichiassist.data.GachaData;
import com.github.unchama.seichiassist.util.Util;

public class gachaCommand implements TabExecutor{
	public SeichiAssist plugin;
	Sql sql = SeichiAssist.plugin.sql;


	public gachaCommand(SeichiAssist plugin){
		this.plugin = plugin;
	}
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command,
			String label, String[] args) {
		return null;
	}
	// /gacha set 0.01 (現在手にもってるアイテムが確率0.01でガチャに出現するように設定）
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("このコマンドはゲーム内から実行してください。");
			return true;
		}



		Player player = (Player) sender;

		if(args.length == 0){
			return false;
		}else if(args[0].equalsIgnoreCase("mente")){
				//menteフラグ反転処理

				//メッセージフラグを反転
				SeichiAssist.gachamente = !SeichiAssist.gachamente;
				if (SeichiAssist.gachamente){
					sender.sendMessage(ChatColor.GREEN + "ガチャシステムを一時停止しました");
				}else{
					sender.sendMessage(ChatColor.GREEN + "ガチャシステムを再開しました");
				}
				return true;
		}else if(args[0].equalsIgnoreCase("reload")){
			//gacha load と入力したとき
			//sender.sendMessage("現在サーバーに登録されているガチャ景品リスト、その他各設定値を最新のconfig.ymlのデータを使って置き換えます");
			//sender.sendMessage("例:config.ymlをエディタで直接編集した後、その内容をゲーム内に反映させる時に使う");
			//SeichiAssist.gachadatalist.clear();
			//SeichiAssist.config.reloadConfig();
			if(!sql.loadGachaData()){
				sender.sendMessage("mysqlからガチャデータのロードできませんでした");
			}else{
				sender.sendMessage("mysqlからガチャデータをロードしました");
			}
			return true;
		}else if(args[0].equalsIgnoreCase("loadfromyml")){
			//config.ymlから読み込む(mysql移行用コマンド)
			SeichiAssist.gachadatalist.clear();
			SeichiAssist.config.loadGachaData();
			sender.sendMessage("config.ymlからガチャデータをロードしました");
			sender.sendMessage("/gacha saveかプラグインをリロードするとmysqlに保存されます");
			return true;

		}else if(args[0].equalsIgnoreCase("save")){
			//gacha save と入力したとき
			//sender.sendMessage("現在サーバーに登録されているガチャ景品データ、及び各設定値を使ってconfig.ymlを置き換えます");
			//sender.sendMessage("例:gachaコマンドでガチャ景品リストを弄った後、変更結果をconfig.ymlに反映させる時に使う");
			//SeichiAssist.config.saveGachaData();
			//SeichiAssist.config.saveConfig();
			if(!sql.saveGachaData()){
				sender.sendMessage("mysqlにガチャデータを保存できませんでした");
			}else{
				sender.sendMessage("mysqlにガチャデータを保存しました");
			}
			return true;

		}else if(args[0].equalsIgnoreCase("add")){
			if(args.length != 2){
				sender.sendMessage("/gacha add 0.05  のように、追加したいアイテムの出現確率を入力してください");
				return true;
			}
			double probability = Util.toDouble(args[1]);
			Gachaadd(player,probability);
			return true;
		}else if(args[0].equalsIgnoreCase("remove")){
			if(args.length != 2){
				sender.sendMessage("/gacha remove 2 のように、削除したいリスト番号を入力してください");
				return true;
			}
			int num = Util.toInt(args[1]);
			Gacharemove(player,num);
			return true;
		}else if(args[0].equalsIgnoreCase("setamount")){
			if(args.length != 3){
				sender.sendMessage("/gacha setamount 2 1 のように、変更したいリスト番号と変更後のアイテム個数を入力してください");
				return true;
			}
			int num = Util.toInt(args[1]);
			int amount = Util.toInt(args[2]);
			GachaEditAmount(player,num,amount);
			return true;
		}else if(args[0].equalsIgnoreCase("setprob")){
			if(args.length != 3){
				sender.sendMessage("/gacha setprob 2 1 のように、変更したいリスト番号と変更後の確率を入力してください");
				return true;
			}
			int num = Util.toInt(args[1]);
			int probability = Util.toInt(args[2]);
			GachaEditProbability(player,num,probability);
			return true;
		}else if(args[0].equalsIgnoreCase("move")){
			if(args.length != 3){
				sender.sendMessage("/gacha move 2 10 のように、変更したいリスト番号と変更後のリスト番号を入力してください");
				return true;
			}
			int num = Util.toInt(args[1]);
			int tonum = Util.toInt(args[2]);
			GachaMove(player,num,tonum);
			return true;
		}else if(args[0].equalsIgnoreCase("list")){
			if(args.length != 1){
				sender.sendMessage("/gacha list で現在登録されているガチャアイテムを全て表示します");
			}
			if(SeichiAssist.gachadatalist.isEmpty()){
				sender.sendMessage("ガチャが設定されていません");
				return true;
			}
			Gachalist(player);
			return true;
		}else if(args[0].equalsIgnoreCase("clear")){
			if(args.length != 1){
				sender.sendMessage("/gacha clear で現在登録されているガチャアイテムを削除します");
			}
			Gachaclear(player);
			return true;
		}

		return false;
	}




	private void Gachaadd(Player player,double probability) {
		GachaData gachadata = new GachaData();
		PlayerInventory inventory = player.getInventory();
		gachadata.itemstack = inventory.getItemInMainHand();
		gachadata.amount = inventory.getItemInMainHand().getAmount();
		gachadata.probability = probability;

		SeichiAssist.gachadatalist.add(gachadata);
		player.sendMessage(gachadata.itemstack.getType().toString() + "/" + gachadata.itemstack.getItemMeta().getDisplayName() + ChatColor.RESET + gachadata.amount + "個を確率" + gachadata.probability + "としてガチャに追加しました");
		player.sendMessage("/gacha saveかプラグインをリロードするとmysqlに保存されます");
	}
	private void Gachalist(Player player){
		int i = 1;
		double totalprobability = 0.0;
		player.sendMessage(ChatColor.RED + "アイテム番号|アイテム名|アイテム数|出現確率");
		for (GachaData gachadata : SeichiAssist.gachadatalist) {
			player.sendMessage(i + "|" + gachadata.itemstack.getType().toString() + "/" + gachadata.itemstack.getItemMeta().getDisplayName() + ChatColor.RESET + "|" + gachadata.amount + "|" + gachadata.probability + "(" + (gachadata.probability*100) + "%)");
			totalprobability += gachadata.probability;
			i++;
		}
		player.sendMessage(ChatColor.RED + "合計確率:" + totalprobability + "(" + (totalprobability*100) + "%)");
		player.sendMessage(ChatColor.RED + "合計確率は100%以内に収まるようにしてください");
	}
	private void Gacharemove(Player player,int num) {
		if(num < 1 || SeichiAssist.gachadatalist.size() < num){
			player.sendMessage("アイテム番号が間違っているようです");
			return;
		}
		GachaData gachadata = SeichiAssist.gachadatalist.get(num-1);
		SeichiAssist.gachadatalist.remove(num-1);
		player.sendMessage(num + "|" + gachadata.itemstack.getType().toString() + "/" + gachadata.itemstack.getItemMeta().getDisplayName() + ChatColor.RESET + "|" + gachadata.amount + "|" + gachadata.probability + "を削除しました");
		player.sendMessage("/gacha saveかプラグインをリロードするとmysqlに保存されます");
	}
	private void GachaEditAmount(Player player,int num,int amount) {
		if(num < 1 || SeichiAssist.gachadatalist.size() < num){
			player.sendMessage("アイテム番号が間違っているようです");
			return;
		}
		GachaData gachadata = SeichiAssist.gachadatalist.get(num-1);
		gachadata.amount = amount;
		SeichiAssist.gachadatalist.set(num-1,gachadata);
		player.sendMessage(num + "|" + gachadata.itemstack.getType().toString() + "/" + gachadata.itemstack.getItemMeta().getDisplayName() + ChatColor.RESET + "のアイテム数を" + gachadata.amount + "個に変更しました");
	}
	private void GachaEditProbability(Player player,int num,int probability) {
		if(num < 1 || SeichiAssist.gachadatalist.size() < num){
			player.sendMessage("アイテム番号が間違っているようです");
			return;
		}
		GachaData gachadata = SeichiAssist.gachadatalist.get(num-1);
		gachadata.probability = probability;
		SeichiAssist.gachadatalist.set(num-1,gachadata);
		player.sendMessage(num + "|" + gachadata.itemstack.getType().toString() + "/" + gachadata.itemstack.getItemMeta().getDisplayName() + ChatColor.RESET + "の確率を" + gachadata.probability + "個に変更しました");
		player.sendMessage("/gacha saveかプラグインをリロードするとmysqlに保存されます");
	}
	private void GachaMove(Player player,int num,int tonum) {
		if(num < 1 || SeichiAssist.gachadatalist.size() < num){
			player.sendMessage("アイテム番号が間違っているようです");
			return;
		}
		if(tonum < 1 || SeichiAssist.gachadatalist.size() < tonum){
			player.sendMessage("アイテム番号が間違っているようです");
			return;
		}
		GachaData gachadata = SeichiAssist.gachadatalist.get(num-1);
		SeichiAssist.gachadatalist.remove(num-1);
		SeichiAssist.gachadatalist.add(tonum-1,gachadata);
		player.sendMessage(num + "|" + gachadata.itemstack.getType().toString() + "/" + gachadata.itemstack.getItemMeta().getDisplayName() + ChatColor.RESET + "をリスト番号" + tonum + "番に移動しました");
		player.sendMessage("/gacha saveかプラグインをリロードするとmysqlに保存されます");
	}
	private void Gachaclear(Player player) {
		SeichiAssist.gachadatalist.clear();
		player.sendMessage("すべて削除しました");
		player.sendMessage("/gacha saveかプラグインをリロードするとmysqlのデータも全削除されます");
		player.sendMessage("削除を取り消すには/gacha reloadコマンドを実行します");
	}


}
