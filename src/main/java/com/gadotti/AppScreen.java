package com.gadotti;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class AppScreen extends JFrame {

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
        executeButton.setIcon(new ImageIcon("src/main/resources/play-button.png"));
        executePanel.add(executeButton);

        // Campo para saída do terminal
        JTextArea terminalOutput = new JTextArea(12, 30); // Define um tamanho específico
        terminalOutput.setEditable(false);

        // Adicionando painéis ao frame
        add(topPanel, BorderLayout.NORTH);
        add(executePanel, BorderLayout.CENTER);
        add(new JScrollPane(terminalOutput), BorderLayout.SOUTH);
    }


}
