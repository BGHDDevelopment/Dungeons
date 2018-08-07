/*     */ package me.ItsJasonn.Dungeons.Utils;
/*     */ 
/*     */ import java.text.DecimalFormat;
/*     */ 
/*     */ public class DateCalculator {
/*     */   public static float getDays(int units) {
/*   7 */     DecimalFormat dm = new DecimalFormat("#.#");
/*   8 */     return Float.parseFloat(dm.format(units / 86400));
/*     */   }
/*     */   
/*  11 */   public static float getHours(int units) { DecimalFormat dm = new DecimalFormat("#.#");
/*  12 */     return Float.parseFloat(dm.format(units / 3600));
/*     */   }
/*     */   
/*  15 */   public static float getMinutes(int units) { DecimalFormat dm = new DecimalFormat("#.#");
/*  16 */     return Float.parseFloat(dm.format(units / 60));
/*     */   }
/*     */   
/*  19 */   public static String getHMS(int units, boolean words) { int s = 0;
/*  20 */     int m = 0;
/*  21 */     int h = 0;
/*     */     
/*  23 */     for (int i = 0; i < units; i++) {
/*  24 */       s++;
/*  25 */       if (s == 60) {
/*  26 */         s = 0;
/*  27 */         m++;
/*     */       }
/*  29 */       if (m == 60) {
/*  30 */         m = 0;
/*  31 */         h++;
/*     */       }
/*     */     }
/*     */     
/*  35 */     String sS = Integer.toString(s);
/*  36 */     String mS = Integer.toString(m);
/*  37 */     String hS = Integer.toString(h);
/*  38 */     if (!words) {
/*  39 */       if (s < 10) {
/*  40 */         sS = "0" + s;
/*     */       }
/*  42 */       if (m < 10) {
/*  43 */         mS = "0" + m;
/*     */       }
/*  45 */       if (h < 10) {
/*  46 */         hS = "0" + h;
/*     */       }
/*     */     }
/*     */     
/*  50 */     if (!words)
/*  51 */       return hS + ":" + mS + ":" + sS;
/*  52 */     if (words) {
/*  53 */       return hS + " hour(s), " + mS + " minute(s), " + sS + " second(s)";
/*     */     }
/*  55 */     return "-1";
/*     */   }
/*     */   
/*  58 */   public static String getDHMS(int units, boolean words) { int s = 0;
/*  59 */     int m = 0;
/*  60 */     int h = 0;
/*  61 */     int d = 0;
/*     */     
/*  63 */     for (int i = 0; i < units; i++) {
/*  64 */       s++;
/*  65 */       if (s == 60) {
/*  66 */         s = 0;
/*  67 */         m++;
/*     */       }
/*  69 */       if (m == 60) {
/*  70 */         m = 0;
/*  71 */         h++;
/*     */       }
/*  73 */       if (h == 24) {
/*  74 */         h = 0;
/*  75 */         d++;
/*     */       }
/*     */     }
/*     */     
/*  79 */     String sS = Integer.toString(s);
/*  80 */     String mS = Integer.toString(m);
/*  81 */     String hS = Integer.toString(h);
/*  82 */     String dS = Integer.toString(d);
/*  83 */     if (!words) {
/*  84 */       if (s < 10) {
/*  85 */         sS = "0" + s;
/*     */       }
/*  87 */       if (m < 10) {
/*  88 */         mS = "0" + m;
/*     */       }
/*  90 */       if (h < 10) {
/*  91 */         hS = "0" + h;
/*     */       }
/*  93 */       if (d < 10) {
/*  94 */         dS = "0" + d;
/*     */       }
/*     */     }
/*     */     
/*  98 */     if (!words)
/*  99 */       return dS + ":" + hS + ":" + mS + ":" + sS;
/* 100 */     if (words) {
/* 101 */       return dS + " day(s), " + hS + " hour(s), " + mS + " minute(s), " + sS + " second(s)";
/*     */     }
/* 103 */     return "-1";
/*     */   }
/*     */   
/* 106 */   public static int getHMS(int units, String type) { int s = 0;
/* 107 */     int m = 0;
/* 108 */     int h = 0;
/*     */     
/* 110 */     for (int i = 0; i < units; i++) {
/* 111 */       s++;
/* 112 */       if (s == 60) {
/* 113 */         s = 0;
/* 114 */         m++;
/*     */       }
/* 116 */       if (m == 60) {
/* 117 */         m = 0;
/* 118 */         h++;
/*     */       }
/*     */     }
/*     */     
/* 122 */     if (type.equalsIgnoreCase("s"))
/* 123 */       return s;
/* 124 */     if (type.equalsIgnoreCase("m"))
/* 125 */       return m;
/* 126 */     if (type.equalsIgnoreCase("h")) {
/* 127 */       return h;
/*     */     }
/* 129 */     return -1;
/*     */   }
/*     */   
/* 132 */   public static String getMS(int units, boolean words) { int s = 0;
/* 133 */     int m = 0;
/*     */     
/* 135 */     for (int i = 0; i < units; i++) {
/* 136 */       s++;
/* 137 */       if (s == 60) {
/* 138 */         s = 0;
/* 139 */         m++;
/*     */       }
/*     */     }
/*     */     
/* 143 */     String sS = Integer.toString(s);
/* 144 */     String mS = Integer.toString(m);
/* 145 */     if (!words) {
/* 146 */       if (s < 10) {
/* 147 */         sS = "0" + s;
/*     */       }
/* 149 */       if (m < 10) {
/* 150 */         mS = "0" + m;
/*     */       }
/*     */     }
/*     */     
/* 154 */     if (!words)
/* 155 */       return mS + ":" + sS;
/* 156 */     if (words) {
/* 157 */       return mS + " minute(s), " + sS + " second(s)";
/*     */     }
/* 159 */     return "-1";
/*     */   }
/*     */ }


/* Location:              C:\Users\JJ\Downloads\Minecraft\Plugins\Dungeons.jar!\me\ItsJasonn\Dungeons\Utils\DateCalculator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */