/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsontoui;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Thanura
 */
public class UI_generator extends JFrame {

    public UI_generator(String JSON, HashMap<String, String> data) {
        JSONObject obj = new JSONObject(JSON);
        JSONArray arr = obj.getJSONArray("UI");
        int width = obj.getInt("width") + 40;
        int height = obj.getInt("height") + 25;
        boolean packed = obj.getBoolean("packed");
        String caption = obj.getString("text");
        setTitle(caption);
        setSize(width, height);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        for (int i = 0; i < arr.length(); i++) {
            String type_ui = arr.getJSONObject(i).getString("type");
            int x = arr.getJSONObject(i).getInt("x");
            int y = arr.getJSONObject(i).getInt("y");
            int uwidth = arr.getJSONObject(i).getInt("width");
            int uheight = arr.getJSONObject(i).getInt("height");
            if (type_ui.toLowerCase().equals("lable")) {
                String text = arr.getJSONObject(i).getString("text");
                text = replace_paras(data, text);
                JLabel jl = new JLabel(text);
                getContentPane().add(jl);
                jl.setBounds(x, y, uwidth, uheight);
            } else if (type_ui.toLowerCase().equals("button")) {
                String text = arr.getJSONObject(i).getString("text");
                String onclick = arr.getJSONObject(i).getString("onclick");
                text = replace_paras(data, text);
                JButton jl = new JButton(text);
                getContentPane().add(jl);
                jl.addActionListener(new onclick(onclick) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        onclick(script);
                    }
                });
                jl.setBounds(x, y, uwidth, uheight);
            } else if (type_ui.toLowerCase().equals("edittext")) {
                String text = arr.getJSONObject(i).getString("text");
                text = replace_paras(data, text);
                JTextField jt = new JTextField(text);
                getContentPane().add(jt);
                jt.setBounds(x, y, uwidth, uheight);
            }
        }
        if (packed) {
            pack();
        }
        setVisible(true);
    }

    String replace_paras(HashMap<String, String> data, String s) {
        if (data == null) {
            return s;
        }
        for (Map.Entry<String, String> e : data.entrySet()) {
            String key = e.getKey();
            String value = e.getValue();
            s = s.replaceAll("@" + key, value);
        }
        return s;
    }

    void onclick(String scripts) {
        String script_lines[] = scripts.split("\n");
        for (String script : script_lines) {
            String command = script.split("=")[0];
            if (script.split("=").length > 1) {
                String data = script.split("=")[1];
                switch (command.toLowerCase()) {
                    case "alert":
                        String atribs[] = data.split(",");
                        if (atribs.length > 1) {
                            switch (atribs[0].toLowerCase()) {
                                case "error":
                                    JOptionPane.showMessageDialog(this, atribs[2], atribs[1],JOptionPane.ERROR_MESSAGE);
                                    break;
                                case "warn":
                                    JOptionPane.showMessageDialog(this, atribs[2], atribs[1],JOptionPane.WARNING_MESSAGE);
                                    break;
                                case "info":
                                    JOptionPane.showMessageDialog(this, atribs[2], atribs[1],JOptionPane.INFORMATION_MESSAGE);
                                    break;
                            }
                        }else{
                            JOptionPane.showMessageDialog(this, data);
                        }
                        break;
                }
            }
        }
    }

    public static void main(String args[]) {
        String json = "{\n"
                + "  \"width\": \"400\",\n"
                + "  \"height\": \"400\",\n"
                + "  \"packed\": false,\n"
                + "  \"text\": \"testing\",\n"
                + "  \"UI\": [\n"
                + "    {\n"
                + "      \"x\": \"310\",\n"
                + "      \"y\": \"10\",\n"
                + "      \"width\": \"20\",\n"
                + "      \"height\": \"20\",\n"
                + "      \"type\": \"lable\",\n"
                + "      \"text\": \"lol\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"x\": \"200\",\n"
                + "      \"y\": \"10\",\n"
                + "      \"width\": \"80\",\n"
                + "      \"height\": \"30\",\n"
                + "      \"type\": \"button\",\n"
                + "      \"onclick\": \"alert=heyalert=new;\",\n"
                + "      \"text\": \"loltest\"\n"
                + "    }\n"
                + "  ]\n"
                + "}";

        UI_generator ui = new UI_generator(json, null);
    }
}
