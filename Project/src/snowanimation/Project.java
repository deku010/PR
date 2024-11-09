import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class Project extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private ArrayList<Snowflake> snowflakes;
    private Image backgroundImage;
    private Image logoImage;
    private Random random;
    
    public Project() {
        setTitle("Snow Animation with Logo");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        snowflakes = new ArrayList<>();
        random = new Random();
        
        // Загрузка изображений
        try {
            backgroundImage = new ImageIcon(getClass().getResource("/snowanimation/background.jpg")).getImage();
            logoImage = new ImageIcon(getClass().getResource("/snowanimation/logo.png")).getImage();
            
            // Вывод информации о загрузке для отладки
            System.out.println("Background loaded: " + (backgroundImage != null));
            System.out.println("Logo loaded: " + (logoImage != null));
        } catch (Exception e) {
            System.out.println("Error loading images: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Создаём начальные снежинки
        for (int i = 0; i < 100; i++) {
            snowflakes.add(new Snowflake());
        }
        
        // Создаём таймер для анимации
        Timer timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSnowflakes();
                repaint();
            }
        });
        timer.start();
    }
    
    private void updateSnowflakes() {
        for (Snowflake snowflake : snowflakes) {
            snowflake.update();
            if (snowflake.y > HEIGHT) {
                snowflake.reset();
            }
        }
    }
    
    @Override
    public void paint(Graphics g) {
        // Создаём буфер для двойной буферизации
        Image offscreen = createImage(WIDTH, HEIGHT);
        Graphics2D g2d = (Graphics2D) offscreen.getGraphics();
        
        // Включаем сглаживание
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Рисуем фон
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, WIDTH, HEIGHT, null);
        } else {
            // Если фон не загрузился, заполняем красным цветом
            g2d.setColor(Color.RED);
            g2d.fillRect(0, 0, WIDTH, HEIGHT);
        }
        
        // Рисуем логотип сверху по центру
        if (logoImage != null) {
            int logoWidth = logoImage.getWidth(null);
            int logoHeight = logoImage.getHeight(null);
            
            // Уменьшаем размер логотипа
            int newLogoWidth = logoWidth / 8;  // уменьшаем в 4 раза
            int newLogoHeight = logoHeight / 9;
            
            // Размещаем логотип по центру вверху
            int logoX = (WIDTH - newLogoWidth) / 2;
            int logoY = 50; // отступ сверху
            
            g2d.drawImage(logoImage, logoX, logoY, newLogoWidth, newLogoHeight, null);
        }
        
        // Рисуем снежинки
        g2d.setColor(Color.WHITE);
        for (Snowflake snowflake : snowflakes) {
            g2d.fillOval((int)snowflake.x, (int)snowflake.y, snowflake.size, snowflake.size);
        }
        
        // Выводим финальное изображение на экран
        g.drawImage(offscreen, 0, 0, this);
    }
    
    // Внутренний класс для снежинок
    private class Snowflake {
        double x, y;
        double speed;
        int size;
        double angle;
        
        public Snowflake() {
            reset();
        }
        
        public void reset() {
            x = random.nextInt(WIDTH);
            y = -10;
            speed = 1 + random.nextDouble() * 3; // Увеличили максимальную скорость
            size = 2 + random.nextInt(4);
            angle = random.nextDouble() * Math.PI * 2;
        }
        
        public void update() {
            y += speed;
            x += Math.sin(angle) * 0.8; // Увеличили амплитуду колебаний
            angle += 0.02; // Увеличили скорость колебаний
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Project().setVisible(true);
            }
        });
    }
}