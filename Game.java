package com.artj.noob;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
//import java.io.File;
import java.io.IOException;


public class Game extends JFrame{
    private static Game window;
    private static Image background; // объявляем переменные метода Image
    private static Image fall;
    private static Image fall_small;
    private static Image game_over;
    private static Image replay;
    private static long last_frame_time; // время между кадрами
    private static float fall_left = 200; // координата x левого верхнего угла капли
    private static float fall_top = -100; // координата y левого верхнего угла капли
//    private static float fall_small_left = 100; //
//    private static float fall_small_top = -50; //
    private static float fall_velocity = 200; // скорость
    private static float replay_left = 760;
    private static float replay_top = 22;
    private static int score;
    private boolean a;



    public static void main(String[] args) throws IOException {
        background = ImageIO.read(Game.class.getResourceAsStream("background.png"));
        fall = ImageIO.read(Game.class.getResourceAsStream("fall.png"));
        game_over = ImageIO.read(Game.class.getResourceAsStream("game_over.png"));
        replay = ImageIO.read(Game.class.getResourceAsStream("replay.png"));
        fall_small = ImageIO.read(Game.class.getResourceAsStream("fall_small.png"));
        window = new Game();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //завершение приложение по закрытию окна
        window.setLocation(200, 100); //точка в которой будет появляться окно в px
        window.setSize(906, 478); //размер окна px
        window.setResizable(false); //запрет на изменение размера окна
        window.setTitle("Game");
        last_frame_time = System.nanoTime();

        GameField game_field = new GameField();
        game_field.addMouseListener(new MouseAdapter() { //клик мыши
            @Override
            public void mousePressed(MouseEvent e){
                int x = e.getX();
                int y = e.getY();
                float fall_right = fall_left + window.getFallSmall().getWidth(null);
                float fall_bottom = fall_top + window.getFallSmall().getHeight(null);
                float replay_right = replay_left + replay.getWidth(null);
                float replay_bottom = replay_top + replay.getHeight(null);
                boolean is_fall = x >= fall_left && x <= fall_right && y >= fall_top && y <= fall_bottom;
                boolean is_replay = x >= replay_left && x <= replay_right && y >= replay_top && y <= replay_bottom;

                if (is_fall) {
                    fall_top = -100;
                    fall_left = (int) (Math.random() * (game_field.getWidth() - window.getFallSmall().getWidth(null)));
                    fall_velocity = fall_velocity + score/2;
                    score++;
                }

                if (window.a && is_replay) {
                    score = 0;
//                       StringBuffer a  = new StringBuffer("\"");
//                       String b = new File(".").getAbsolutePath();
//                       System.out.println(a.append(b.replace('\\', '/')).append("out/artifacts/Catch_jar/Catch.jar").append("\""));
//                       System.out.println("111. " + Runtime.getRuntime().exec( "\"" + a.replace('\\', '/') + "out/artifacts/Catch_jar/Catch.jar" + "\"").waitFor());

//                       try {
//                           restartApp();
//                       }
//                       catch (IOException e1) {
//                           e1.printStackTrace();
//                       }
                }
            }
        });
        window.add(game_field);
        window.setVisible(true); //делаем окно видимым
    }


    private static void onRepaint(Graphics g){
        long current_time = System.nanoTime();
        float delta_time = (current_time - last_frame_time) * 0.000000001f;
        last_frame_time = current_time;
        fall_top = fall_top + fall_velocity * delta_time;
        g.drawImage(background, 0, 0, null);
        g.drawImage(window.getFallSmall(), (int) fall_left, (int) fall_top, null);
        window.a = fall_top > window.getHeight();
        if(window.a){// если координата капли выходит за пределы окна, то выводим гейм овер
            g.drawImage(game_over, 280, 120, null);
            g.drawImage(replay, (int) (replay_left), (int) replay_top, null);
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.ORANGE);
        Font ft = new Font("SansSerif", Font.ITALIC,30);
        g2.setFont(ft);
        String sc = Integer.toString(score);
        g2.drawString(sc, 10,33);
    }


    private static class GameField extends JPanel{

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            onRepaint(g);
            repaint();
            // вызвать метод как можно чаще
        }
    }

    public Image getFallSmall(){
        if (score < 10){
            return fall;
        }
        else return fall_small;
    }

//    public float getSmallLeft(){
//        if (score < 10){
//            return fall_left;
//        }
//        else return fall_small_left;
//    }
//
//    public float getSmallTop(){
//        if (score < 10){
//            return fall_top;
//        }
//        else return fall_small_top;
//    }
}