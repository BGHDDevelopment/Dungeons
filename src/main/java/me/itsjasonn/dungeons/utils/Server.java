/*    */ package me.itsjasonn.dungeons.utils;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.DataOutputStream;
/*    */ import me.itsjasonn.dungeons.main.Plugin;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ 
/*    */ public class Server
/*    */ {
/* 11 */   private String server = "";
/*    */   
/* 13 */   public Server(String server) { this.server = server; }
/*    */   
/*    */   public void sendPlayerToServer(Player player) {
/* 16 */     ByteArrayOutputStream b = new ByteArrayOutputStream();
/* 17 */     DataOutputStream out = new DataOutputStream(b);
/*    */     try {
/* 19 */       out.writeUTF("Connect");
/* 20 */       out.writeUTF(this.server);
/*    */     } catch (Exception e) {
/* 22 */       e.printStackTrace();
/*    */     }
/* 24 */     player.sendPluginMessage(Plugin.core, "BungeeCord", b.toByteArray());
/*    */   }
/*    */ }


/* Location:              C:\Users\JJ\Downloads\Minecraft\Plugins\Dungeons.jar!\me\ItsJasonn\Dungeons\Utils\Server.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */