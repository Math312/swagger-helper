package com.sharedaka.ui.requester;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBTabbedPane;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RequestMethodPanel extends JPanel {

    private JLabel titleLabel;

    private ComboBox httpMethodLabel;

    private JTextArea urlTextArea;

    private JButton commitButton;

    private RequestBodyPanel responseBodyPanel;

    public RequestMethodPanel() {
        Box box = Box.createVerticalBox();
        this.add(box);
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.setBackground(JBColor.WHITE);


        Box titleBar = Box.createHorizontalBox();
        titleBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.add(titleBar);
        JLabel titleLabel = new JLabel("Post");
        titleLabel.setBackground(Gray._247);
        titleLabel.setForeground(Gray._94);
        titleLabel.setOpaque(true);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setFont(new Font("Simsun", Font.BOLD, 13));
        titleBar.add(titleLabel);
        this.titleLabel = titleLabel;


        box.add(Box.createRigidArea(new Dimension(0, 10)));


        Box urlBar = Box.createHorizontalBox();
        urlBar.setAlignmentX(LEFT_ALIGNMENT);
        box.add(urlBar);
        ComboBox<String> httpMethodLabel = new ComboBox<String>();
        httpMethodLabel.setBackground(Gray._247);
        httpMethodLabel.setForeground(Gray._94);
        httpMethodLabel.setBorder(BorderFactory.createLineBorder(Gray._221));
        httpMethodLabel.setFont(new Font("Simsun", Font.BOLD, 15));
        httpMethodLabel.setMinimumSize(new Dimension(80, 35));
        httpMethodLabel.setMaximumSize(new Dimension(80, 35));
        httpMethodLabel.addItem("GET");
        httpMethodLabel.addItem("POST");
        httpMethodLabel.addItem("PUT");
        httpMethodLabel.addItem("DELETE");
        this.httpMethodLabel = httpMethodLabel;


        JTextArea urlTextArea = new JTextArea(3, 50);
        urlTextArea.setAutoscrolls(true);
        urlTextArea.setLineWrap(true);
        urlTextArea.setMinimumSize(new Dimension(450, 30));
        urlTextArea.setMaximumSize(new Dimension(450, 30));
        urlTextArea.setBackground(Gray._247);
        this.urlTextArea = urlTextArea;

        JButton commitButton = new JButton("Send");
        commitButton.setMaximumSize(new Dimension(80, 30));
        commitButton.setMinimumSize(new Dimension(80, 30));
        commitButton.setBorder(BorderFactory.createLineBorder(JBColor.LIGHT_GRAY, 1));
        commitButton.setSize(80, 30);
        this.commitButton = commitButton;
        this.commitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doRequest();
            }
        });


        urlBar.add(httpMethodLabel);
        urlBar.add(urlTextArea);
        urlBar.add(commitButton);


        Box requestsBox = Box.createHorizontalBox();
        box.add(requestsBox);
        requestsBox.setAlignmentX(LEFT_ALIGNMENT);
        JBTabbedPane requestContentPane = new JBTabbedPane();
        requestsBox.add(requestContentPane);
        requestContentPane.addTab("Headers", new RequestParamPanel());
        requestContentPane.addTab("Param", new RequestParamPanel());
        requestContentPane.addTab("body", new RequestBodyPanel());

        box.add(Box.createRigidArea(new Dimension(0, 10)));


        Box responseBox = Box.createVerticalBox();
        box.add(responseBox);
        responseBox.setAlignmentX(LEFT_ALIGNMENT);
        JBTabbedPane responseContentPane = new JBTabbedPane();
        responseBox.add(responseContentPane);
        RequestBodyPanel responseBodyPanel = new RequestBodyPanel();
        responseContentPane.addTab("Headers", new ResponseHeaderPanel());
        responseContentPane.addTab("body", responseBodyPanel);
        this.responseBodyPanel = responseBodyPanel;


    }


    private void doRequest() {
        String url = this.urlTextArea.getText();
        if (!StringUtils.startsWithIgnoreCase(url, "https://") || !StringUtils.startsWithIgnoreCase(url, "http://")) {
            url = "http://" + url;
        }
        String method = (String) this.httpMethodLabel.getSelectedItem();
        String uri = null;
        String host = null;
        try {
            URL url1 = new URL(url);
            uri = url1.getPath();
            host = url1.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        assert method != null;
        HttpRequestBase httpRequestBase = getHttpRequestBase(method, uri);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpHost httpHost = new HttpHost(host);
        try {
            CloseableHttpResponse response = httpClient.execute(httpHost, httpRequestBase);
            StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer, StandardCharsets.UTF_8.name());
            String responseStr = null;
            if (StringUtils.isNotBlank(writer.toString())) {
                try {
                    Document doc = Jsoup.parseBodyFragment(writer.toString());
                    responseStr = doc.body().html();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.responseBodyPanel.setBody(responseStr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private HttpRequestBase getHttpRequestBase(String method, String uri) {
        HttpRequestBase httpRequestBase = null;
        if (uri == null) {
            uri = "/";
        }
        switch (method) {
            case "GET":
                httpRequestBase = new HttpGet(uri);
                break;
            case "POST":
                httpRequestBase = new HttpPost(uri);
                break;
            case "PUT":
                httpRequestBase = new HttpPut(uri);
                break;
            case "DELETE":
                httpRequestBase = new HttpDelete(uri);
                break;
            default:
                httpRequestBase = new HttpGet(uri);
        }
        return httpRequestBase;
    }
}
