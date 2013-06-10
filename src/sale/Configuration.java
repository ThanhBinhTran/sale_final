/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sale;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 *
 * @author Binh
 */
public class Configuration extends javax.swing.JFrame {

    /**
     * Creates new form Configuration
     */
    String file_dir = "data";
    String  file_path = file_dir + "\\configuration.thsv";
    public Configuration() {
        initComponents();
    }

    public boolean checknumber()
    {
        boolean return_val = true;
        try{
            Integer.parseInt(align_stt.getText().trim());
            Integer.parseInt(align_tsp.getText().trim());
            Integer.parseInt(align_sl.getText().trim());
            Integer.parseInt(align_tien.getText().trim());
            Integer.parseInt(align_money.getText().trim());
            Integer.parseInt(bill_maxNameChar.getText().trim());
        }catch(NumberFormatException err)
        {
            return_val = false;
        }
        return return_val;
    }
    public void writeConfigurationfile()
    {
        
        try
        {
            // Create file 
            File Fdir = new File(file_dir);
            boolean  a = Fdir.mkdirs(); 
            if(a || Fdir.isDirectory())
            {
                FileOutputStream fos = new FileOutputStream(file_path);
                Writer out = new OutputStreamWriter(fos, "UTF8");
                out.write("=00" + bill_name.getText().trim() + "\n");
                out.write("=01" + bill_phone.getText().trim() + "\n");
                out.write("=02" + bill_separation1.getText().trim() + "\n");
                out.write("=03" + bill_numofitems.getText().trim() + "\n");
                out.write("=04" + bill_subtotal.getText().trim() + "\n");
                out.write("=05" + bill_remained.getText().trim() + "\n");
                out.write("=06" + bill_separation2.getText().trim() + "\n");
                out.write("=07" + bill_total.getText().trim() + "\n");
                out.write("=08" + align_stt.getText().trim() + "<<>>" +
                                  align_tsp.getText().trim() + "<<>>" +
                                  align_sl.getText().trim() +"<<>>" +
                                  align_tien.getText().trim() +"<<>>" +
                                  align_money.getText().trim() + "\n");
                out.write("=09" + bill_maxNameChar.getText().trim() + "\n");
                out.close();
            }
        thongbao.setText("Đã lưa");
        thongbao.setForeground(Color.blue);
        }
        catch (Exception e){//Catch exception if any
            thongbao.setText("Lỗi file");
            thongbao.setForeground(Color.red);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        bill_name = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        bill_phone = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        bill_separation1 = new javax.swing.JTextField();
        bill_numofitems = new javax.swing.JTextField();
        bill_subtotal = new javax.swing.JTextField();
        bill_separation2 = new javax.swing.JTextField();
        bill_total = new javax.swing.JTextField();
        bill_remained = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        align_sl = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        align_tien = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        align_stt = new javax.swing.JTextField();
        align_tsp = new javax.swing.JTextField();
        bill_separation3 = new javax.swing.JTextField();
        align_money = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        bill_maxNameChar = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        thongbao = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTabbedPane2.setAutoscrolls(true);
        jTabbedPane2.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 0, 0));
        jLabel1.setText("Tiêu đề:");

        bill_name.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        bill_name.setForeground(new java.awt.Color(153, 0, 0));
        bill_name.setText("Tạp hóa SÁU VÂN");
        bill_name.setToolTipText("");
        bill_name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bill_nameActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 0, 0));
        jLabel2.setText("Điện thoại");

        bill_phone.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        bill_phone.setForeground(new java.awt.Color(153, 0, 0));
        bill_phone.setText("0987-886-119");
        bill_phone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bill_phoneActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 0, 0));
        jLabel3.setText("Đường cách");

        bill_separation1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        bill_separation1.setForeground(new java.awt.Color(153, 0, 0));
        bill_separation1.setText("-----------------------------------------------------------");
        bill_separation1.setEnabled(false);
        bill_separation1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bill_separation1ActionPerformed(evt);
            }
        });

        bill_numofitems.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        bill_numofitems.setForeground(new java.awt.Color(153, 0, 0));
        bill_numofitems.setText("số lượng: ");
        bill_numofitems.setEnabled(false);

        bill_subtotal.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        bill_subtotal.setForeground(new java.awt.Color(153, 0, 0));
        bill_subtotal.setText("Thành tiền: ");
        bill_subtotal.setEnabled(false);
        bill_subtotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bill_subtotalActionPerformed(evt);
            }
        });

        bill_separation2.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        bill_separation2.setForeground(new java.awt.Color(153, 0, 0));
        bill_separation2.setText("--------------------------------------------");
        bill_separation2.setEnabled(false);
        bill_separation2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bill_separation2ActionPerformed(evt);
            }
        });

        bill_total.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        bill_total.setForeground(new java.awt.Color(153, 0, 0));
        bill_total.setText("Tổng");
        bill_total.setEnabled(false);

        bill_remained.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        bill_remained.setForeground(new java.awt.Color(153, 0, 0));
        bill_remained.setText("Nợ cũ:");
        bill_remained.setEnabled(false);

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel4.setText("canh lề(vị trí cuối):");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel5.setText("SL");

        align_sl.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        align_sl.setText("80");
        align_sl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                align_slActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel6.setText("Tiền(1000đ)");

        align_tien.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        align_tien.setText("130");
        align_tien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                align_tienActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel7.setText("canh lề(vị trí đầu)");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel8.setText("Stt");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel9.setText("Tên sản phẩm");

        align_stt.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        align_stt.setText("-70");
        align_stt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                align_sttActionPerformed(evt);
            }
        });

        align_tsp.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        align_tsp.setText("-55");
        align_tsp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                align_tspActionPerformed(evt);
            }
        });

        bill_separation3.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        bill_separation3.setForeground(new java.awt.Color(153, 0, 0));
        bill_separation3.setText("-----------------------------------------------------------");
        bill_separation3.setEnabled(false);
        bill_separation3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bill_separation3ActionPerformed(evt);
            }
        });

        align_money.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        align_money.setText("30");
        align_money.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                align_moneyActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel10.setText("Số chữ tối đa của tên sản phẩm:");

        bill_maxNameChar.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        bill_maxNameChar.setText("20");
        bill_maxNameChar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bill_maxNameCharActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(align_stt, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(align_tsp))
                                        .addGap(20, 20, 20)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(align_sl, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(bill_maxNameChar, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                    .addComponent(align_tien)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(bill_name))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(bill_phone))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(bill_separation1, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(align_money, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(bill_remained, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(bill_total)
                                .addComponent(bill_separation2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(bill_subtotal))))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(bill_numofitems, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bill_separation3, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {bill_remained, bill_separation2, bill_subtotal, bill_total});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bill_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(bill_phone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(bill_separation1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(align_sl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(align_tien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(align_stt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(align_tsp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bill_maxNameChar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addComponent(bill_separation3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bill_numofitems, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bill_subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bill_remained, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(align_money, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bill_separation2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bill_total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {bill_name, bill_numofitems, bill_phone, bill_remained, bill_separation1, bill_separation2, bill_subtotal, bill_total, jLabel1, jLabel2, jLabel3});

        jTabbedPane2.addTab("Giấy in", jPanel1);

        jButton1.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jButton1.setText("Lưa lại");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        thongbao.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        thongbao.setText("Thông báo:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(thongbao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(thongbao))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bill_nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bill_nameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bill_nameActionPerformed

    private void bill_subtotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bill_subtotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bill_subtotalActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        boolean result = checknumber();
        if(result){
        writeConfigurationfile();
        }else {
            thongbao.setText("Nhập số sai, chỉ được nhập số nguyên");
        }
            
    }//GEN-LAST:event_jButton1ActionPerformed

    private void bill_separation1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bill_separation1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bill_separation1ActionPerformed

    private void bill_separation2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bill_separation2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bill_separation2ActionPerformed

    private void bill_phoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bill_phoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bill_phoneActionPerformed

    private void align_slActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_align_slActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_align_slActionPerformed

    private void align_tienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_align_tienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_align_tienActionPerformed

    private void align_sttActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_align_sttActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_align_sttActionPerformed

    private void align_tspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_align_tspActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_align_tspActionPerformed

    private void bill_separation3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bill_separation3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bill_separation3ActionPerformed

    private void align_moneyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_align_moneyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_align_moneyActionPerformed

    private void bill_maxNameCharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bill_maxNameCharActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bill_maxNameCharActionPerformed

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
            java.util.logging.Logger.getLogger(Configuration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Configuration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Configuration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Configuration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Configuration().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField align_money;
    private javax.swing.JTextField align_sl;
    private javax.swing.JTextField align_stt;
    private javax.swing.JTextField align_tien;
    private javax.swing.JTextField align_tsp;
    private javax.swing.JTextField bill_maxNameChar;
    private javax.swing.JTextField bill_name;
    private javax.swing.JTextField bill_numofitems;
    private javax.swing.JTextField bill_phone;
    private javax.swing.JTextField bill_remained;
    private javax.swing.JTextField bill_separation1;
    private javax.swing.JTextField bill_separation2;
    private javax.swing.JTextField bill_separation3;
    private javax.swing.JTextField bill_subtotal;
    private javax.swing.JTextField bill_total;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JLabel thongbao;
    // End of variables declaration//GEN-END:variables
}
