package com.github.unchama.seichiassist.listener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.github.unchama.seichiassist.ActiveSkill;
import com.github.unchama.seichiassist.Config;
import com.github.unchama.seichiassist.SeichiAssist;
import com.github.unchama.seichiassist.data.PlayerData;
import com.github.unchama.seichiassist.util.ExperienceManager;
import com.github.unchama.seichiassist.util.Util;

public class PlayerInventoryListener implements Listener {
	HashMap<UUID,PlayerData> playermap = SeichiAssist.playermap;
	private Config config = SeichiAssist.config;

	/*
	//プレイヤーが4次元ポケットを閉じた時に実行
	@EventHandler
	public void onPlayerPortalCloseEvent(InventoryCloseEvent event){
		HumanEntity he = event.getPlayer();
		Inventory inventory = event.getInventory();

		//インベントリを開けたのがプレイヤーではない時終了
		if(!he.getType().equals(EntityType.PLAYER)){
			return;
		}
		//インベントリサイズが２７でない時終了
		if(inventory.getSize() != 27){
			return;
		}
		if(inventory.getTitle().equals(ChatColor.DARK_PURPLE + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "4次元ポケット")){
			Player player = (Player)he;
			PlayerInventory pinventory = player.getInventory();
			ItemStack itemstack = pinventory.getItemInMainHand();
			if(itemstack.getType().equals(Material.ENDER_PORTAL_FRAME)){
				//閉まる音を再生
				player.playSound(player.getLocation(), Sound.BLOCK_ENDERCHEST_CLOSE, 1, (float) 0.1);
			}
		}
	}
	*/

	@EventHandler
	public void onPlayerClickActiveSkillSellectEvent(InventoryClickEvent event){
		ItemStack itemstackcurrent = event.getCurrentItem();
		InventoryView view = event.getView();
		Inventory topinventory = view.getTopInventory();
		HumanEntity he = view.getPlayer();

		//インベントリを開けたのがプレイヤーではない時終了
		if(!he.getType().equals(EntityType.PLAYER)){
			return;
		}
		Player player = (Player)he;
		UUID uuid = player.getUniqueId();
		//経験値変更用のクラスを設定
		ExperienceManager expman = new ExperienceManager(player);
		//インベントリが存在しない時終了
		if(topinventory == null){
			return;
		}

		//インベントリサイズが36でない時終了
		if(topinventory.getSize() != 36){
			return;
		}

		//外枠のクリック処理なら終了
		if(event.getClickedInventory() == null){
			return;
		}



		//インベントリ名が以下の時処理
		if(topinventory.getTitle().equals(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "木の棒メニュー")){
			event.setCancelled(true);
			if(event.getClickedInventory().getType().equals(InventoryType.PLAYER)){
				return;
			}
			PlayerData playerdata = playermap.get(uuid);
			if(itemstackcurrent.getType().equals(Material.COAL_ORE)){
				if(playerdata.activenum == ActiveSkill.DUALBREAK.getNum()){

				}else if(playerdata.level >= config.getDualBreaklevel()){
					playerdata.activenum = ActiveSkill.DUALBREAK.getNum();
					player.sendMessage(ChatColor.GREEN + "アクティブスキル:デュアルブレイク");
					playerdata.activemineflagnum = 1;
					player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, (float) 0.1);
				}else{
					player.sendMessage(ChatColor.GREEN + "必要整地レベルが足りません。");
					player.playSound(player.getLocation(), Sound.BLOCK_GLASS_PLACE, 1, (float) 0.1);
				}
			}

			else if(itemstackcurrent.getType().equals(Material.IRON_ORE)){
				if(playerdata.activenum == ActiveSkill.TRIALBREAK.getNum()){

				}else if(playerdata.level >= config.getTrialBreaklevel() && playerdata.activenum != ActiveSkill.TRIALBREAK.getNum()){
					playerdata.activenum = ActiveSkill.TRIALBREAK.getNum();
					player.sendMessage(ChatColor.GREEN + "アクティブスキル:トリアルブレイク");
					playerdata.activemineflagnum = 1;
					player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, (float) 0.1);
				}else{
					player.sendMessage(ChatColor.GREEN + "必要整地レベルが足りません。");
					player.playSound(player.getLocation(), Sound.BLOCK_GLASS_PLACE, 1, (float) 0.1);
				}
			}

			else if(itemstackcurrent.getType().equals(Material.GOLD_ORE)){
				if(playerdata.activenum == ActiveSkill.EXPLOSION.getNum()){

				}else if(playerdata.level >= config.getExplosionlevel() && playerdata.activenum != ActiveSkill.EXPLOSION.getNum()){
					playerdata.activenum = ActiveSkill.EXPLOSION.getNum();
					player.sendMessage(ChatColor.GREEN + "アクティブスキル:エクスプロージョン");
					playerdata.activemineflagnum = 1;
					player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, (float) 0.1);
				}else{
					player.sendMessage(ChatColor.GREEN + "必要整地レベルが足りません。");
					player.playSound(player.getLocation(), Sound.BLOCK_GLASS_PLACE, 1, (float) 0.1);
				}
			}

			else if(itemstackcurrent.getType().equals(Material.REDSTONE_ORE)){
				if(playerdata.activenum == ActiveSkill.THUNDERSTORM.getNum()){

				}else if(playerdata.level >= config.getThunderStormlevel() && playerdata.activenum != ActiveSkill.THUNDERSTORM.getNum()){
					playerdata.activenum = ActiveSkill.THUNDERSTORM.getNum();
					player.sendMessage(ChatColor.GREEN + "アクティブスキル:サンダーストーム");
					playerdata.activemineflagnum = 1;
					player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, (float) 0.1);
				}else{
					player.sendMessage(ChatColor.GREEN + "必要整地レベルが足りません。");
					player.playSound(player.getLocation(), Sound.BLOCK_GLASS_PLACE, 1, (float) 0.1);
				}
			}

			else if(itemstackcurrent.getType().equals(Material.LAPIS_ORE)){
				if(playerdata.activenum == ActiveSkill.BLIZZARD.getNum()){

				}else if(playerdata.level >= config.getBlizzardlevel() && playerdata.activenum != ActiveSkill.BLIZZARD.getNum()){
					playerdata.activenum = ActiveSkill.BLIZZARD.getNum();
					player.sendMessage(ChatColor.GREEN + "アクティブスキル:ブリザード");
					playerdata.activemineflagnum = 1;
					player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, (float) 0.1);
				}else{
					player.sendMessage(ChatColor.GREEN + "必要整地レベルが足りません。");
					player.playSound(player.getLocation(), Sound.BLOCK_GLASS_PLACE, 1, (float) 0.1);
				}
			}

			else if(itemstackcurrent.getType().equals(Material.EMERALD_ORE)){
				if(playerdata.activenum == ActiveSkill.METEO.getNum()){

				}else if(playerdata.level >= config.getMeteolevel() && playerdata.activenum != ActiveSkill.METEO.getNum()){
					playerdata.activenum = ActiveSkill.METEO.getNum();
					player.sendMessage(ChatColor.GREEN + "アクティブスキル:メテオ");
					playerdata.activemineflagnum = 1;
					player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, (float) 0.1);
				}else{
					player.sendMessage(ChatColor.GREEN + "必要整地レベルが足りません。");
					player.playSound(player.getLocation(), Sound.BLOCK_GLASS_PLACE, 1, (float) 0.1);
				}
			}

			//溜まったガチャ券をインベントリへ
			else if(itemstackcurrent.getType().equals(Material.SKULL_ITEM) && ((SkullMeta)itemstackcurrent.getItemMeta()).getOwner().equals("unchama")){
				ItemStack skull = Util.getskull(Util.getName(player));
				int count = 0;
				while(playerdata.gachapoint >= config.getGachaPresentInterval()){
					playerdata.gachapoint -= config.getGachaPresentInterval();
					if(player.getInventory().contains(skull) || !Util.isPlayerInventryFill(player)){
						Util.addItem(player,skull);
					}else{
						Util.dropItem(player,skull);
					}
					count++;
				}
				//プレイヤーデータを更新
				playerdata.lastgachapoint = playerdata.gachapoint;

				if(count > 0){
					player.sendMessage(ChatColor.GOLD + "ガチャ券" + count + "枚" + ChatColor.WHITE + "プレゼントフォーユー");
					player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);
				}

				ItemMeta itemmeta = itemstackcurrent.getItemMeta();
				List<String> lore = Arrays.asList(ChatColor.RESET + "" +  ChatColor.RED + "獲得できるガチャ券はありません"
						, ChatColor.RESET + "" +  ChatColor.AQUA + "次のガチャ券まで:" + (int)(1000 - playerdata.gachapoint%1000) + "ブロック");
				itemmeta.setLore(lore);
				itemstackcurrent.setItemMeta(itemmeta);
			}

			//経験値を消費してプレイヤーの頭を召喚
			else if(itemstackcurrent.getType().equals(Material.SKULL_ITEM) && ((SkullMeta)itemstackcurrent.getItemMeta()).getOwner().equals("MHF_Villager")){
				//経験値変更用のクラスを設定
				//経験値が足りなかったら処理を終了
				if(!expman.hasExp(10000)){
					player.sendMessage(ChatColor.RED + "必要な経験値が足りません");
					return;
				}
				//経験値消費
				expman.changeExp(-10000);

				//プレイヤーの頭作成
				ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1);
				SkullMeta skullmeta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
				skull.setDurability((short) 3);
				skullmeta.setOwner(player.getName());
				skull.setItemMeta(skullmeta);

				//渡すか、落とすか
				if(player.getInventory().contains(skull) || !Util.isPlayerInventryFill(player)){
					Util.addItem(player,skull);
				}else{
					Util.dropItem(player,skull);
				}
				player.sendMessage(ChatColor.GOLD + "経験値10000を消費して自分の頭を召喚しました");
				player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);
			}

			else if(itemstackcurrent.getType().equals(Material.STONE_BUTTON)){
				playerdata.gachaflag = !playerdata.gachaflag;
				if(playerdata.gachaflag){
					player.sendMessage(ChatColor.GREEN + "毎分のガチャ券受け取り:ON");
					player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, 1);
					ItemMeta itemmeta = itemstackcurrent.getItemMeta();
					List<String> lore = Arrays.asList(ChatColor.RESET + "" +  ChatColor.GREEN + "毎分受け取っています"
							, ChatColor.RESET + "" +  ChatColor.DARK_RED + "" + ChatColor.UNDERLINE + "クリックすると変更できます"
							);
					itemmeta.setLore(lore);
					itemstackcurrent.setItemMeta(itemmeta);
				}else{
					player.sendMessage(ChatColor.RED + "毎分のガチャ券受け取り:OFF");
					player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 1, 1);
					ItemMeta itemmeta = itemstackcurrent.getItemMeta();
					List<String> lore = Arrays.asList(ChatColor.RESET + "" +  ChatColor.RED + "毎分受け取っていません"
							, ChatColor.RESET + "" +  ChatColor.DARK_RED + "" + ChatColor.UNDERLINE + "クリックすると変更できます"
							);
					itemmeta.setLore(lore);
					itemstackcurrent.setItemMeta(itemmeta);
				}
			}

			else if(itemstackcurrent.getType().equals(Material.DIAMOND_PICKAXE)){
				// ver0.3.2 採掘速度上昇効果トグル
				playerdata.effectflag = !playerdata.effectflag;
				if(playerdata.effectflag){
					player.sendMessage(ChatColor.GREEN + "採掘速度上昇効果:ON");
					player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, 1);
					ItemMeta itemmeta = itemstackcurrent.getItemMeta();
					List<String> lore = Arrays.asList(ChatColor.RESET + "" +  ChatColor.GREEN + "現在ONになっています"
							, ChatColor.RESET + "" +  ChatColor.DARK_RED + "" + ChatColor.UNDERLINE + "クリックすると変更できます"
							, ChatColor.RESET + "" +  ChatColor.GRAY + "採掘速度上昇効果とは"
							, ChatColor.RESET + "" +  ChatColor.GRAY + "現在の接続人数と過去1分間の採掘量に応じて"
							, ChatColor.RESET + "" +  ChatColor.GRAY + "採掘速度が変化するシステムです"
							, ChatColor.RESET + "" +  ChatColor.GRAY + "" + ChatColor.UNDERLINE + "/ef smart"
							, ChatColor.RESET + "" +  ChatColor.GRAY + "で効果の内訳を表示できます"
							);
					itemmeta.setLore(lore);
					itemstackcurrent.setItemMeta(itemmeta);
				}else{
					player.sendMessage(ChatColor.RED + "採掘速度上昇効果:OFF");
					player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 1, 1);
					ItemMeta itemmeta = itemstackcurrent.getItemMeta();
					List<String> lore = Arrays.asList(ChatColor.RESET + "" +  ChatColor.RED + "現在OFFになっています"
							, ChatColor.RESET + "" +  ChatColor.DARK_RED + "" + ChatColor.UNDERLINE + "クリックすると変更できます"
							, ChatColor.RESET + "" +  ChatColor.GRAY + "採掘速度上昇効果とは"
							, ChatColor.RESET + "" +  ChatColor.GRAY + "現在の接続人数と過去1分間の採掘量に応じて"
							, ChatColor.RESET + "" +  ChatColor.GRAY + "採掘速度が変化するシステムです"
							, ChatColor.RESET + "" +  ChatColor.GRAY + "" + ChatColor.UNDERLINE + "/ef smart"
							, ChatColor.RESET + "" +  ChatColor.GRAY + "で効果の内訳を表示できます"
							);
					itemmeta.setLore(lore);
					itemstackcurrent.setItemMeta(itemmeta);
				}
			}

			else if(itemstackcurrent.getType().equals(Material.BEACON)){
				// spawnコマンド実行
				player.chat("/spawn");
				player.closeInventory();
			}

			else if(itemstackcurrent.getType().equals(Material.BED)){
				// sethomeコマンド実行
				player.chat("/sethome");
			}

			else if(itemstackcurrent.getType().equals(Material.COMPASS)){
				// homeコマンド実行
				player.chat("/home");
				player.closeInventory();
			}

			else if(itemstackcurrent.getType().equals(Material.WOOD_AXE)){
				// wand召喚
				player.chat("//wand");
				player.closeInventory();
				player.sendMessage(ChatColor.RESET + "" +  ChatColor.DARK_GREEN + "" + ChatColor.UNDERLINE + "保護のかけ方\n"
						+ ChatColor.RESET + "" +  ChatColor.GREEN + "①召喚された斧を手に持ちます\n"
						+ ChatColor.RESET + "" +  ChatColor.GREEN + "②保護したい領域の一方の角を" + ChatColor.YELLOW + "左" + ChatColor.GREEN + "クリック\n"
						+ ChatColor.RESET + "" +  ChatColor.GREEN + "③もう一方の対角線上の角を" + ChatColor.RED + "右" + ChatColor.GREEN + "クリック\n"
						+ ChatColor.RESET + "" +  ChatColor.GREEN + "③メニューの「保護領域の申請」ボタンをクリック\n"
						+ ChatColor.DARK_GREEN + "解説ページ→" + ChatColor.UNDERLINE + "http://seichi.click/d/WorldGuard"
						);
			}

			else if(itemstackcurrent.getType().equals(Material.GOLD_AXE)){
				// 保護の設定

				/*
				if(!WGBukkit.getPlugin().canBuild(player, player.getLocation())){
					player.sendMessage(ChatColor.RED + "" + ChatColor.UNDERLINE + "別の保護と被ってます");
					return;
				}
				*/
				player.closeInventory();
				player.sendMessage(ChatColor.GRAY + "木の斧で選択されている範囲で保護の設定を行います…");
						player.sendMessage(ChatColor.GRAY + "(//expand vert→/rg claim " + player.getName() + "_" + playerdata.rgnum + "→//cel)");
				player.chat("//expand vert");
				player.chat("/rg claim " + player.getName() + "_" + playerdata.rgnum);
				playerdata.rgnum += 1;
				player.chat("//sel");
			}

			else if(itemstackcurrent.getType().equals(Material.STONE_AXE)){
				// 保護リストの表示
				player.closeInventory();
				player.sendMessage(ChatColor.GRAY + "現在の保護の一覧を表示します…(/rg list)");
				player.sendMessage(ChatColor.GRAY + "複数ページある場合は"
				+ ChatColor.RESET + "" +  ChatColor.GRAY + "" + ChatColor.UNDERLINE + "/rg list ページNo"
				+ ChatColor.RESET + "" +  ChatColor.GRAY + " で2ページ目以降を開いてください"
				);
				player.chat("/rg list");
				player.sendMessage(ChatColor.RESET + "" +  ChatColor.GRAY + "" + ChatColor.UNDERLINE + "/rg remove 保護名"
				+ ChatColor.RESET + "" +  ChatColor.GRAY + " で保護の削除が出来ます");
			}


			else if(itemstackcurrent.getType().equals(Material.NETHER_STAR)){
				// hubコマンド実行
				// player.chat("/hub");
				player.closeInventory();
				player.sendMessage(ChatColor.RESET + "" +  ChatColor.GRAY + "Tキーを押して/hubと入力してEnterキーを押してください");
			}


			else if(itemstackcurrent.getType().equals(Material.BOOK)){
				// wikiリンク表示
				player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.UNDERLINE + "http://seichi.click");
				player.closeInventory();
			}

			else if(itemstackcurrent.getType().equals(Material.BOOK_AND_QUILL)){
				// 投票リンク表示
				player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.UNDERLINE + "https://minecraft.jp/servers/seichi.click/vote");
				player.closeInventory();
			}

			else if(itemstackcurrent.getType().equals(Material.PAPER)){
				// 運営方針とルールリンク表示
				player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.UNDERLINE + "http://seichi.click/d/%b1%bf%b1%c4%ca%fd%bf%cb%a4%c8%a5%eb%a1%bc%a5%eb");
				player.closeInventory();
			}

			else if(itemstackcurrent.getType().equals(Material.MAP)){
				// 鯖マップリンク表示
				player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.UNDERLINE + "http://mc.seichi.click:8123");
				player.closeInventory();

			}

			else if(itemstackcurrent.getType().equals(Material.SIGN)){
				// 掲示板リンク表示
				player.sendMessage(ChatColor.DARK_GRAY + "開いたら下の方までスクロールしてください\n"
						+ ChatColor.DARK_RED + "" + ChatColor.UNDERLINE + "https://minecraft.jp/servers/seichi.click"
						);
				player.closeInventory();
			}

			else if(itemstackcurrent.getType().equals(Material.ENDER_PORTAL_FRAME)){
				//ver0.3.2 四次元ポケットを開く
				//パッシブスキル[4次元ポケット]（PortalInventory）を発動できるレベルに達していない場合処理終了
				if( playerdata.level < SeichiAssist.config.getPassivePortalInventorylevel()){
					player.sendMessage(ChatColor.GREEN + "4次元ポケットを開くには整地レベルが"+SeichiAssist.config.getPassivePortalInventorylevel()+ "以上必要です。");
					return;
				}
				//開く音を再生
				player.playSound(player.getLocation(), Sound.BLOCK_ENDERCHEST_OPEN, 1, (float) 0.1);
				//インベントリを開く
				player.openInventory(playerdata.inventory);
			}

			/*
			else if(itemstackcurrent.getType().equals(Material.DIAMOND_ORE)){
				if(playerdata.activenum == ActiveSkill.GRAVITY.getNum()){

				}else if(playerdata.level >= config.getGravitylevel() && playerdata.activenum != ActiveSkill.GRAVITY.getNum()){
					playerdata.activenum = ActiveSkill.GRAVITY.getNum();
					player.sendMessage(ChatColor.GREEN + "アクティブスキル:グラビティ");
					playerdata.activemineflagnum = 1;
					player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, (float) 0.1);
				}else{
					player.sendMessage(ChatColor.GREEN + "必要整地レベルが足りません。");
					player.playSound(player.getLocation(), Sound.BLOCK_GLASS_PLACE, 1, (float) 0.1);
				}
			}
			*/

		}
	}

	/*
	//プレイヤーがアクティブスキル選択インベントリを閉じた時に実行
	@EventHandler
	public void onPlayerActiveSkillSellectCloseEvent(InventoryCloseEvent event){
		HumanEntity he = event.getPlayer();
		Inventory inventory = event.getInventory();

		//インベントリを開けたのがプレイヤーではない時終了
		if(!he.getType().equals(EntityType.PLAYER)){
			return;
		}
		//インベントリサイズが36でない時終了
		if(inventory.getSize() != 36){
			return;
		}
		if(inventory.getTitle().equals(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "木の棒メニュー")){
			Player player = (Player)he;
			PlayerInventory pinventory = player.getInventory();
			ItemStack itemstack = pinventory.getItemInMainHand();
			if(itemstack.getType().equals(Material.STICK)){
				//閉まる音を再生
				player.playSound(player.getLocation(), Sound.BLOCK_FENCE_GATE_CLOSE, 1, (float) 0.1);
			}
		}
	}
	*/


/*バグ確認のため未実装
	//インベントリに4次元ポケットを入れられないようにする。
	@EventHandler
	public void onPlayerClickPortalInventoryEvent(InventoryClickEvent event){
		ItemStack itemstackcursor = event.getCursor();
		ItemStack itemstackcurrent = event.getCurrentItem();
		Inventory inventory = event.getClickedInventory();

		if(inventory == null){
			return;
		}
		if(!inventory.getType().equals(InventoryType.PLAYER)){
			if(itemstackcursor.getType().equals(Material.ENDER_PORTAL_FRAME) || itemstackcurrent.getType().equals(Material.ENDER_PORTAL_FRAME)){
				event.setCancelled(true);
			}
		}

	}

	//ドロップできないようにする。
	@EventHandler
	public void onPlayerDropPortalInventoryEvent(PlayerDropItemEvent event){
		Item item = event.getItemDrop();
		ItemStack itemstack = item.getItemStack();
		if(itemstack.getType().equals(Material.ENDER_PORTAL_FRAME)){
			event.setCancelled(true);
		}
	}
*/

}