package src.main.java.com.gadotti;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class AppScreen extends JFrame {
    JTextArea terminalOutput;

    public AppScreen() {
        // Configurações iniciais da janela
        setTitle("Download do Youtube");
        setSize(1200, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Adicionando componentes
        addComponents();

        // Tornando a tela visível
        setVisible(true);
    }

    private void addComponents() {
        // Painel para a parte superior da tela
        JPanel topPanel = new JPanel(new GridLayout(2, 1)); // Grid com 2 linhas para URL e Pasta Destino

        // Campo de texto para URL
        JPanel urlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel urlLabel = new JLabel("Cole o link aqui: ");
        JTextField urlField = new JTextField(60); // Tamanho ajustado para ser similar ao diretório
        urlPanel.add(urlLabel);
        urlPanel.add(urlField);

        // Campo de texto para seleção de diretório
        JPanel directoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel directoryLabel = new JLabel("Pasta Destino: ");
        JTextField directoryField = new JTextField(60); // Tamanho ajustado para ser igual ao campo de URL
        JButton directoryButton = new JButton("Selecionar");
        directoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File(".")); // Define o diretório atual como o diretório do usuário
                chooser.setDialogTitle("Escolha a Pasta Destino");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Permite apenas a seleção de diretórios
                chooser.setAcceptAllFileFilterUsed(false); // Desativa a opção de selecionar todos os arquivos

                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    directoryField.setText(chooser.getSelectedFile().getAbsolutePath()); // Define o campo de texto com o caminho do diretório
                } else {
                    System.out.println("Nenhuma seleção de pasta foi feita.");
                }
            }
        });
        directoryPanel.add(directoryLabel);
        directoryPanel.add(directoryField);
        directoryPanel.add(directoryButton);

        // Adiciona os painéis de URL e diretório ao painel superior
        topPanel.add(urlPanel);
        topPanel.add(directoryPanel);

        // Botão de executar com ícone de play
        JPanel executePanel = new JPanel();
        JButton executeButton = new JButton("Baixar");
        executeButton.setMinimumSize(new Dimension(10,5));
        //executeButton.setIcon(new ImageIcon("src/main/resources/play-button.png"));
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    terminalOutput.setText("Iniciando Download...... \n ");
                    terminalOutput.setForeground(Color.BLACK);
                });

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = urlField.getText();
                        String pastaDestino = directoryField.getText();
                        if (url.indexOf("?list") > -1){
                            url = url.substring(0, url.indexOf("?list"));
                        }
                        if (url.indexOf("&list") > -1){
                            url = url.substring(0, url.indexOf("&list"));
                        }
                        System.out.println(url);

                        // Verifica se os campos não estão vazios
                        if (url.isEmpty() || pastaDestino.isEmpty()) {
                            if (url.isEmpty()) {
                                appendToTerminalOutput("Link faltante !!!", Color.RED);
                            }
                            if (pastaDestino.isEmpty()) {
                                appendToTerminalOutput("Pasta destino faltante !!!", Color.RED);
                            }
                        } else {
                            // Chama o método baixar e verifica o status
                            STATUS status = baixar(pastaDestino, url);
                            if (status == STATUS.SUCESSO) {
                                appendToTerminalOutput("Download concluído com sucesso.", Color.GREEN);
                            } else {
                                appendToTerminalOutput("Falha no download.", Color.RED);
                            }
                        }
                    }
                }).start();
            }
        });


        executePanel.add(executeButton);

        // Campo para saída do terminal
        terminalOutput = new JTextArea(12, 30); // Define um tamanho específico
        terminalOutput.setEditable(false);

        // Adicionando painéis ao frame
        add(topPanel, BorderLayout.NORTH);
        add(executePanel, BorderLayout.CENTER);
        add(new JScrollPane(terminalOutput), BorderLayout.SOUTH);
    }

    private void appendToTerminalOutput(String message, Color color) {
        terminalOutput.setForeground(color);
        terminalOutput.append(message + "\n");
    }

    public STATUS baixar(String pastaDestino, String URL) {
        try {
            ProcessBuilder builder = new ProcessBuilder("yt-dlp", "-x", "--audio-format", "mp3", "-P", pastaDestino, URL);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            // Aguarda o processo ser concluído
            int exitCode = process.waitFor();

            // Verifica o código de saída
            if (exitCode == 0) {
                return STATUS.SUCESSO;
            } else {
                return STATUS.FALHA;
            }
        } catch (Exception e) {
            // Imprime a exceção no console e exibe a mensagem de erro
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> appendToTerminalOutput("Erro: " + e.getMessage(), Color.RED));
            return STATUS.FALHA;
        }
    }

    private enum STATUS {
        SUCESSO,
        FALHA
    }

}
