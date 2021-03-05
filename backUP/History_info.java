package sale;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Binh
 */
public class History_info extends javax.swing.JFrame {

    /**
     * Creates new form history_info
     */
    //final JFileChooser fc;
    // FileNameExtensionFilter filter ;
    Vector  vTime;
    Vector  vTime_name;
    Vector  vSeek_idx;
    String  file_name;
    DefaultTableModel tm;
    DefaultListModel lm ;
    public History_info() {
        initComponents();
        initial_environment();
        setPayTableColumnSize();
    }
    
    public final void initial_environment()
    {
        vTime = new Vector();
        vTime_name  = new Vector();
        vSeek_idx   = new Vector();
        tm =  (DefaultTableModel)table_hoa_don.getModel();
        time_line.setModel(new DefaultListModel());
        lm  = (DefaultListModel) time_line.getModel();
    }
        public final void setPayTableColumnSize()
    {
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment( JLabel.RIGHT );
        table_hoa_don.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 16));
        TableColumn column = null;
        column = table_hoa_don.getColumnModel().getColumn(0);
        column.setPreferredWidth(30);
        column.setCellRenderer(rightRenderer) ;
        column = table_hoa_don.getColumnModel().getColumn(1);
        column.setPreferredWidth(120);
        column = table_hoa_don.getColumnModel().getColumn(2);
        column.setPreferredWidth(200);
        column = table_hoa_don.getColumnModel().getColumn(3);
        column.setPreferredWidth(70);
        column.setCellRenderer(rightRenderer) ;
        column = table_hoa_don.getColumnModel().getColumn(4);
        column.setPreferredWidth(80);
        column.setCellRenderer(rightRenderer) ;             
    }
    public int choosefile()
    {
        int return_val = 0;
        JFileChooser fileChooser = new JFileChooser();
        String choosertitle = "chọn file lưa trữ";
        FileFilter filter = new FileNameExtensionFilter("tạp hóa Sáu Vân", "thsv");
        fileChooser.setFileFilter(filter);
        fileChooser.setCurrentDirectory(new java.io.File("."));
        fileChooser.setDialogTitle(choosertitle);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            field_file_name.setText(fileChooser.getSelectedFile().toString());
        }
        else {
            return_val = -1;
            thongbao.setText("file not found");
        }
        return return_val;
    }
    
    public void read_his_file()
    {
        vSeek_idx.clear();
        vTime.clear();
        vTime_name.clear();
        String[] raw_data;
        try{
            FileInputStream in = new FileInputStream(file_name);
            try (BufferedReader bufffile = new BufferedReader(new InputStreamReader(in, "UTF8"))) 
            {
                String strLine;
                //in.read(strLine);
                
                strLine = bufffile.readLine();
                int iline_num = 0;
                while (strLine != null)   {
                // Print the content on the console
                    iline_num++;
                    if(strLine.substring(0, 3).equals("-->"))
                    {
                        System.out.println("+++++++" + strLine);
                        vSeek_idx.add(iline_num);
                        raw_data = strLine.substring(3).split(" ",2);
                        Ngay_thang.setText(raw_data[0]);
                        vTime_name.add(strLine.substring(3));
                        vTime.add(raw_data[1]);
                    }
                    strLine = bufffile.readLine();
                }
            }
            in.close();
        }catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
        thongbao.setText(file_name);
    }
    
    public boolean check_file(String infile)
    {
        File File_path = new File(infile);
        return File_path.isFile();
    }
    
    public void add2List()
    {
        int bill_count;
        file_name   = field_file_name.getText();
        lm.removeAllElements();
        if(check_file(file_name))
        {
            read_his_file();
            bill_count = vTime.size();
            for(int i = 0; i < bill_count; i++)
            {
                System.out.println(vTime.get(i) + "<>"+ vSeek_idx.get(i));
                lm.addElement(vTime.get(i));
            }
        }
        else
        {
            thongbao.setText("Không tìm thấy file lưa trữ");
        }
        
    }
    public int convert_time2lineIdx(String iString)
    {
        int bill_count = vTime.size();
        int return_val = -1;
        for(int i = 0; i < bill_count; i ++)
        {
            if(vTime.get(i).toString().equals(iString))
            {
                return_val = i;
                break;
            }
        }
        return return_val;
    }
    public int display_billInfo(int iline)
    {
        int return_val = 0;
        String[] idata;
        int data_line = 1;
        //clear table;
        tm.getDataVector().removeAllElements();
        tm.fireTableDataChanged();
        if(check_file(file_name))
        {
            FileInputStream in = null;
            try {
                in = new FileInputStream(file_name);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(History_info.class.getName()).log(Level.SEVERE, null, ex);
            }
            try (BufferedReader bufffile = new BufferedReader(new InputStreamReader(in, "UTF8"))) 
            {
            String strLine;
            strLine = bufffile.readLine();
            int line_count = 0;
            double soluong = 0;
            double subtotal = 0;
            double nocu     = 0;
            double total    = 0;
            while (strLine != null) {
                line_count++;
                {
                    if(line_count >= iline)
                    {
                        Vector data_row = new Vector();
                        idata = strLine.substring(3).split("<>");
                        System.out.println("lenght " + idata.length + "--" + strLine + "--" + line_count);
                        switch (strLine.substring(0, 3)) {
                            case "-->":
                                ten_khach_hang.setText("Khách hàng:" + idata[1]);
                                break;
                            case "---":
                                if (idata.length<4) {
                                    thongbao.setText("ERROR! " + iline);
                                    break;
                                }
                                data_row.add(data_line);
                                data_row.add(idata[0]);   //ma sanpham
                                data_row.add(idata[1]);   //ma sanpham
                                data_row.add(idata[2]);   //ma sanpham
                                data_row.add(idata[3]);   //ma sanpham
                                tm.addRow(data_row);
                                tm.fireTableDataChanged();
                                data_line++;
                                break;
                            case "==>":
                                soluong = Double.parseDouble(idata[0]);
                                subtotal = Double.parseDouble(idata[1]);
                                data_row.add(null);
                                data_row.add(null);
                                data_row.add(null);
                                data_row.add(soluong);   //ma sanpham
                                data_row.add(subtotal);   //ma sanpham
                                tm.addRow(data_row);
                                tm.fireTableDataChanged();
                                break ;
                            case "==-":
                                Vector data_row1 = new Vector();
                                nocu = Double.parseDouble(idata[0]);

                                data_row.add(null);
                                data_row.add(null);
                                data_row.add(null);   
                                data_row.add("Nợ cũ");
                                data_row.add(nocu);   //ma sanpham
                                tm.addRow(data_row);
                                tm.fireTableDataChanged();
                                data_row1.add(null);
                                data_row1.add(null);
                                data_row1.add(null);   //ma sanpham
                                data_row1.add("Tổng cộng");
                                data_row1.add(nocu + subtotal);   //ma sanpham
                                tm.addRow(data_row1);
                                tm.fireTableDataChanged();
                                data_line = -1;
                                break ;
                        }
                        if(data_line == -1)
                            break;
                    }
                    strLine = bufffile.readLine();
                }
            }
            bufffile.close();
            in.close();
        }
        catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
        }
        else
        {
            thongbao.setText("Không tìm thấy file");
        }
        return return_val;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        time_line = new javax.swing.JList();
        field_file_name = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_hoa_don = new javax.swing.JTable();
        thongbao = new javax.swing.JLabel();
        tim_gio = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        Ngay_thang = new javax.swing.JLabel();
        ten_khach_hang = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Xem lại hóa đơn");

        jButton1.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jButton1.setText("Mở file");
        jButton1.setToolTipText("chọn để mở file chứa hóa đơn/ chọn năm/ chọn tháng/ chọn ngày ví dụ 2013/03/21");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        time_line.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        time_line.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        time_line.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                time_lineMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(time_line);

        field_file_name.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        field_file_name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                field_file_nameActionPerformed(evt);
            }
        });

        table_hoa_don.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        table_hoa_don.setForeground(new java.awt.Color(102, 0, 0));
        table_hoa_don.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "stt", "Mã sản phẩm", "Tên sản phẩm", "số lượng", "Thành giá"
            }
        ));
        jScrollPane2.setViewportView(table_hoa_don);

        thongbao.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        thongbao.setText("Thông báo");

        tim_gio.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        tim_gio.setText("Tìm giờ mua/Tên khác hàng");
        tim_gio.setToolTipText("tìm người mua, tìm thời gian mua. hệ thống sẽ liệt kê ra hết mốc thời gian liên qua. giờ.phút. giây");
        tim_gio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tim_gioMouseClicked(evt);
            }
        });
        tim_gio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tim_gioActionPerformed(evt);
            }
        });
        tim_gio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tim_gioKeyReleased(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jButton3.setText("refresh");
        jButton3.setToolTipText("hiển thị lại hết hóa đơn trên list");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        Ngay_thang.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        Ngay_thang.setText("Ngày tháng năm");

        ten_khach_hang.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        ten_khach_hang.setText("Tên khách hàng");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(thongbao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(field_file_name))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(tim_gio, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton3)
                                .addGap(38, 38, 38)
                                .addComponent(Ngay_thang, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ten_khach_hang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(field_file_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(thongbao)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tim_gio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3)
                    .addComponent(Ngay_thang)
                    .addComponent(ten_khach_hang))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        choosefile();
        add2List();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tim_gioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tim_gioMouseClicked
        // TODO add your handling code here:
        tim_gio.setText("");
    }//GEN-LAST:event_tim_gioMouseClicked

    private void tim_gioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tim_gioActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_tim_gioActionPerformed

    private void tim_gioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tim_gioKeyReleased
        // TODO add your handling code here:
        time_line.removeAll();
        String inString = tim_gio.getText().trim();
        int time_count = vTime.size();
        if(!inString.isEmpty())
        {
            thongbao.setText("Đang tìm..." + inString);
            //clear jlist
            lm.removeAllElements();
            for(int i = 0; i < time_count; i++)
            {
                if(vTime_name.get(i).toString().toLowerCase().contains(inString.toLowerCase()))
                {
                    lm.addElement(vTime.get(i));
                }
            }
            if(lm.isEmpty())
            {
                thongbao.setText("Không tìm thấy: " + inString);
            }
            else
            {
                thongbao.setText("Có " + lm.size() + " kết quả: " + inString);
            }
        }           
    }//GEN-LAST:event_tim_gioKeyReleased

    private void field_file_nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_field_file_nameActionPerformed
        // TODO add your handling code here:
        add2List();
    }//GEN-LAST:event_field_file_nameActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        add2List();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void time_lineMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_time_lineMouseClicked
        // TODO add your handling code here:
        String stime  = time_line.getSelectedValue().toString();
        int i = convert_time2lineIdx(stime);
        
        System.out.println("converted i = " + i);
        if(i == -1)
        {
            thongbao.setText("ERROR! convert " + stime);
        }
        else
        {
            int line    = Integer.parseInt(vSeek_idx.get(i).toString());
            display_billInfo(line);
        }
    }//GEN-LAST:event_time_lineMouseClicked
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(History_info.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(History_info.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(History_info.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(History_info.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new History_info().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Ngay_thang;
    private javax.swing.JTextField field_file_name;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable table_hoa_don;
    private javax.swing.JLabel ten_khach_hang;
    private javax.swing.JLabel thongbao;
    private javax.swing.JTextField tim_gio;
    private javax.swing.JList time_line;
    // End of variables declaration//GEN-END:variables
}

