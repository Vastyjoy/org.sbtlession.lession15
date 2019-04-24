package GUI;

import Core.Message;
import Core.Messenger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MainFrame extends JFrame {

    Messenger messager;
    private JTextField jtfMessage;
    private JTextField jtfDestination;
    private JTextArea jtaTextAreaMessage;
    private JLabel jLabelUserName;
    // имя клиента
    private String clientName = "";

    //имя последнего получателя

    private String lastDestinationName;

    // получаем имя клиента
    public String getClientName() {
        return this.clientName;
    }


    // конструктор
    public MainFrame(Messenger messager) {

        this.clientName = clientName;
        this.messager = messager;

        // Задаём настройки элементов на форме
        setBounds(600, 300, 600, 500);
        setTitle("Client");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jtaTextAreaMessage = new JTextArea();
        jtaTextAreaMessage.setEditable(false);
        jtaTextAreaMessage.setLineWrap(true);
        jLabelUserName=new JLabel();
        jLabelUserName.setText(messager.getUserName());
        add(jLabelUserName,BorderLayout.NORTH);
        JScrollPane jsp = new JScrollPane(jtaTextAreaMessage);
        add(jsp, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        add(bottomPanel, BorderLayout.SOUTH);
        JButton jbSendMessage = new JButton("Отправить");
        bottomPanel.add(jbSendMessage, BorderLayout.EAST);
        jtfMessage = new JTextField("Введите ваше сообщение: ");
        bottomPanel.add(jtfMessage, BorderLayout.CENTER);
        jtfDestination = new JTextField("Введите имя получателя: ");
        bottomPanel.add(jtfDestination, BorderLayout.WEST);

        // обработчик события нажатия кнопки отправки сообщения
        jbSendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // если имя клиента, и сообщение непустые, то отправляем сообщение
                if (!jtfMessage.getText().trim().isEmpty() && !jtfDestination.getText().trim().isEmpty()) {
                    lastDestinationName = jtfDestination.getText();
                    String message = jtfMessage.getText();
                    sendMsg(message);
                    // фокус на текстовое поле с сообщением
                    jtfMessage.grabFocus();
                } else if (jtfMessage.getText().trim().isEmpty()) jtfMessage.grabFocus();
                else if(jtfDestination.getText().trim().isEmpty())jtfDestination.grabFocus();
            }
        });

        // при фокусе поле сообщения очищается
        jtfMessage.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jtfMessage.setText("");
            }
        });

        // при фокусе поле имя очищается
        jtfDestination.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jtfDestination.setText("");
            }
        });

        // в отдельном потоке начинаем работу с сервером
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // бесконечный цикл
                    while (!Thread.interrupted()) {
                        // если есть входящее сообщение
                        Thread.sleep(1500);
                        System.err.println("Пытаемся получить сообщения");
                        List<Message> list = messager.recvMessage();
                        System.err.println("получили сообщений:" + list.size());

                        for (Message msg : list) {
                            jtaTextAreaMessage.append(msg.toString());
                        }
                        Thread.sleep(100);
                    }
                } catch (Exception e) {
                }
            }
        });

        // добавляем обработчик события закрытия окна клиентского приложения
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thread.interrupt();
                super.windowClosing(e);

            }
        });

        // отображаем форму
        thread.setDaemon(true);
        this.setVisible(true);
        thread.start();
    }

    // отправка сообщения
    public void sendMsg(String message) {
        // отправляем сообщение
        messager.sendMessage(lastDestinationName, message);
        jtfMessage.setText("");
    }

    protected void showError(Message msg) {
        JFrame jFrame = new JFrame();
        jFrame.setBounds(600, 300, 200, 200);
        jFrame.setTitle("Error");
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setText(msg.getMessage());
        jFrame.add(textArea, BorderLayout.CENTER);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                this.windowClosing(e);

            }
        });
        jFrame.setVisible(true);
        jFrame.setFocusable(true);
    }
}
