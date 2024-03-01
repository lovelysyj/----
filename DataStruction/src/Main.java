import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.Font;
class Node {
    int index;
    int degree;
    int color;
}

class NWayIntersectionPanel extends JPanel {
    private int intersectionCount;

    public void setIntersectionCount(int count) {
        intersectionCount = count;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY) - 20;

        // 计算右上角起始位置
        int startX = 40;
        int startY = 20;

        g.drawOval(startX, startY, radius * 2, radius * 2);

        double angle = 2 * Math.PI / intersectionCount;
        for (int i = 0; i < intersectionCount; i++) {
            double theta = i * angle;
            int x = (int) (startX +radius+ radius * Math.cos(theta));
            int y = (int) (startY +radius+radius * Math.sin(theta));

            g.drawLine(startX+radius, startY+radius, x, y);

            String roadLabel = "Road " + (char) ('A' + i);
            g.drawString(roadLabel, x, y);
        }
    }
}

public class Main extends JFrame {
    private int intersectionCount;
    private int lukouCount=10;

    private JTextField[][] textFieldMatrix;
    private JTextArea outputTextArea;
    private List<String> orderedPairs=new ArrayList<>();

    private NWayIntersectionPanel intersectionPanel;
    private JPanel matrixPanel; // 矩阵面板


    public Main() {
        // 设置窗口标题
        setTitle("多叉路口交通灯的管理--唐振鑫202200300377");
        // 设置窗口关闭操作
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 设置窗口首选大小
        setPreferredSize(new Dimension(1600, 800));

        JPanel mainPanel = new JPanel(new BorderLayout());

        // 创建水平分割面板
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        // 创建左侧面板和右侧面板
        JPanel leftPanel = new JPanel(new BorderLayout());

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(1150, 400)); // 设置rightPanel的首选大小




        // 设置左侧和右侧面板为分割面板的组件
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);



        // 将分割面板添加到主面板的中央位置
        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(rightPanel,BorderLayout.EAST);




        // 创建输入面板
        JPanel inputPanel = new JPanel();
        // 创建输入按钮、标签和文本框
        JButton inputButton = new JButton("输入路口数量");
        JLabel labelOne = new JLabel("路口数量: ");
        JTextField textField = new JTextField(6);

        // 将标签、文本框和按钮添加到输入面板
        inputPanel.add(labelOne);
        inputPanel.add(textField);
        inputPanel.add(inputButton);

        // 添加输入按钮的点击事件监听器
        inputButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = textField.getText();
                try {
                    intersectionCount = Integer.parseInt(input); // 获取输入的路口数
                    refreshRightPanel(lukouCount,rightPanel);
                    generateOrderedPairs(intersectionCount);// 生成路口连接关系数组
                    drawIntersection(); // 绘制路口图
                    createTextFieldMatrix(lukouCount,rightPanel);


                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "输入无效！请重新输入整数值。");
                }
            }
        });

        // 将输入面板和路口连接关系面板添加到左侧面板
        leftPanel.add(inputPanel, BorderLayout.NORTH);

        // 创建路口图形展示面板
        intersectionPanel = new NWayIntersectionPanel();
        // 将路口图形展示面板添加到右侧面板的中央位置
        leftPanel.add(intersectionPanel, BorderLayout.CENTER);


        createTextFieldMatrix(lukouCount,rightPanel);

        // 创建输出文本区域
        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        scrollPane.setPreferredSize(new Dimension(1500, 350)); // 设置rightPanel的首选大小
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); // 总是显示水平滚动条
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // 总是显示垂直滚动条




        add(mainPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);


    }


    private void refreshRightPanel(int count,JPanel rightPanel){
        clearTextFieldMatrix(); // 在创建新的文本字段矩阵之前清除旧的矩阵
        orderedPairs.clear();
        rightPanel.removeAll(); // 清除面板上的所有组件
        rightPanel.revalidate(); // 重新验证面板
        rightPanel.repaint(); // 刷新面板
    }


    private void createTextFieldMatrix(int creatCount, JPanel rightPanel) {


        matrixPanel = new JPanel();
        matrixPanel.setLayout(new GridLayout((creatCount+2), creatCount));

        textFieldMatrix = new JTextField[creatCount][creatCount];



        // 添加列头
        for (int j = 0; j < creatCount; j++) {
            if(orderedPairs.size()==0){
                JLabel label = new JLabel("路线"+(j+1)); // 设置列头标签的文本
                label.setHorizontalAlignment(JLabel.CENTER); // 设置标签居中显示
                matrixPanel.add(label); // 将标签添加到矩阵面板中
            }else{
                JLabel label = new JLabel(orderedPairs.get(j)); // 设置列头标签的文本
                label.setHorizontalAlignment(JLabel.CENTER); // 设置标签居中显示
                matrixPanel.add(label); // 将标签添加到矩阵面板中
            };
        }

        for (int j = 0; j < creatCount; j++) {
            JLabel label = new JLabel("路线"+j); // 设置列头标签的文本
            label.setHorizontalAlignment(JLabel.CENTER); // 设置标签居中显示
            matrixPanel.add(label); // 将标签添加到矩阵面板中
        }

        for (int i = 0; i < creatCount; i++) {
            for (int j = 0; j < creatCount; j++) {
                textFieldMatrix[i][j] = new JTextField(2);
                matrixPanel.add(textFieldMatrix[i][j]);
            }
        }



        JButton solveButton = new JButton("Solve");
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveGraphColoring(creatCount);
            }
        });




        JScrollPane scrollPane = new JScrollPane(matrixPanel); // 创建带有滚动条的容器
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); // 总是显示水平滚动条
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // 总是显示垂直滚动条

        rightPanel.setLayout(new BorderLayout()); // 设置rightPanel的布局为边界布局
        rightPanel.add(scrollPane, BorderLayout.CENTER); // 将滚动容器添加到rightPanel的中央位置
        rightPanel.add(solveButton, BorderLayout.SOUTH); // 将solveButton添加到rightPanel的南边位置


        // 创建按钮
        JButton setOneButton = new JButton("全部置1");
        setOneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setAllValues(1,creatCount);
            }
        });

        JButton setZeroButton = new JButton("全部置0");
        setZeroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setAllValues(0,creatCount);
            }
        });

        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(setOneButton);
        buttonPanel.add(setZeroButton);


        // 将按钮面板添加到右面板
        rightPanel.add(buttonPanel, BorderLayout.NORTH);


        // 自动填充表格所有格子为1
        setAllValues(1,creatCount);

    }

    private void clearTextFieldMatrix() {
        if (textFieldMatrix != null) {
            for (int i = 0; i < textFieldMatrix.length; i++) {
                for (int j = 0; j < textFieldMatrix[i].length; j++) {
                    matrixPanel.remove(textFieldMatrix[i][j]); // 从面板中移除文本字段
                    textFieldMatrix[i][j] = null;
                }
            }
            textFieldMatrix = null;
        }
    }

    //置1/0的
    private void setAllValues(int value,int count) {
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                if (i==j){
                    textFieldMatrix[i][j].setText(String.valueOf(0));
                }else {
                    textFieldMatrix[i][j].setText(String.valueOf(value));
                }
            }
        }
    }

    //获取1/0信号数据
    private int[][] getGraphMatrix(int count) {
        int[][] graph = new int[count][count];
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                String value = textFieldMatrix[i][j].getText();
                graph[i][j] = Integer.parseInt(value);
            }
        }
        return graph;
    }

    //解决路口信号灯问题
    private void solveGraphColoring(int count) {
        outputTextArea.setText(""); // 清空结果文本区域

        // 在solveGraphColoring方法中，修改以下代码来调整字体大小
        Font currentFont = outputTextArea.getFont();
        Font newFont = currentFont.deriveFont(18f); // 16是所需的字体大小
        outputTextArea.setFont(newFont);

        int[][] graph = getGraphMatrix(count);
        Node[] nodes = new Node[count];
        for (int i = 0; i < count; i++) {
            int degree = 0;
            for (int j = 0; j < count; j++) {
                if (graph[i][j] != 0)
                    degree++;
            }
            nodes[i] = new Node();
            nodes[i].index = i;
            nodes[i].degree = degree;
            nodes[i].color = 0;
        }

        sort(nodes,count);
        StringBuilder output = new StringBuilder();
        output.append("按度数由大到小排序后的结果：");
        for (int i = 0; i < count; i++) {
            output.append(nodes[i].index).append(" ");
        }
        output.append("\n");
        //output(output.toString());

        int k = 0;
        while (true) {
            k++;
            int i;
            for (i = 0; i < count; i++) {
                if (nodes[i].color == 0) {
                    nodes[i].color = k;
                    break;
                }
            }
            if (i == count)
                break;
            for (int j = 0; j < count; j++){
                if (ifOk(nodes, graph, k, j))
                    nodes[j].color = k;
            }
        }

        output.append("所需要的最小路灯颜色数目：").append(k - 1).append("\n");
        output.append("一种路灯设置方案为(忽略单向不存在的路线)：(颜色:路线)\n");
        for (int i = 1; i < k; i++) {
            output.append(i).append(':');
            for (int j = 0; j < count; j++)
                if (nodes[j].color == i)
                    output.append(nodes[j].index).append(' ');
            if (i < k - 1)
                output.append("\n");
        }
        output(output.toString());
    }

    private void sort(Node[] nodes,int count) {
        for (int i = 0; i < count; i++) {
            for (int j = (count-1); j > i; j--) {
                if (nodes[j].degree > nodes[j - 1].degree) {
                    int degree = nodes[j - 1].degree;
                    int index = nodes[j - 1].index;
                    nodes[j - 1].degree = nodes[j].degree;
                    nodes[j - 1].index = nodes[j].index;
                    nodes[j].degree = degree;
                    nodes[j].index= index;
                }
            }
        }
    }

    private void output(String message) {
        outputTextArea.append(message);
        outputTextArea.append("\n");
        outputTextArea.setCaretPosition(outputTextArea.getDocument().getLength()); // 滚动到文本区域末尾
    }

    private boolean ifOk(Node[] nodes, int[][] graph, int color, int j) {
        if (nodes[j].color != 0)
            return false;
        for (int i = 0; i < lukouCount; i++)
            if (graph[nodes[j].index][i] == 1)
                for (int m = 0; m < lukouCount; m++)
                    if (nodes[m].index == i)
                        if (nodes[m].color == color)
                            return false;
        return true;
    }

    private void generateOrderedPairs(int creat) {

        String[] roadLabels = new String[creat]; // 创建一个数组来存储道路标签

        for (int i = 0; i < creat; i++) {
            roadLabels[i] = "Road " + (char) ('A' + i); // 分配道路标签，如 "Road A"、"Road B" 等
        }

        // 为所有道路组合生成有序对
        for (int i = 0; i < creat; i++) {
            for (int j = i + 1; j < creat; j++) {
                String orderedPair1 = "(" + roadLabels[i] + ", " + roadLabels[j] + ")"; // 以 "(道路1, 道路2)" 的格式创建有序对
                String orderedPair2 = "(" + roadLabels[j] + ", " + roadLabels[i] + ")"; // 以 "(道路2, 道路1)" 的格式创建反向有序对
                orderedPairs.add(orderedPair1); // 将有序对添加到列表中
                orderedPairs.add(orderedPair2); // 将反向有序对添加到列表中
            }
        }
        //System.out.println(orderedPairs.toString());
        lukouCount=orderedPairs.size();


    }

    private void drawIntersection() {
        //System.out.println(intersectionCount);
        intersectionPanel.setIntersectionCount(intersectionCount); // 设置交叉口面板的交叉口数量
        intersectionPanel.repaint(); // 重绘交叉口面板
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        });
    }
}