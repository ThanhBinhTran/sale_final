/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sale;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Binh
 */
public final class danh_ba_dien_thoai extends javax.swing.JFrame {

    /**
     * Creates new form danh_ba_dien_thoai
     */
    Vector  Phone_number;
    Vector  Phone_name;
    Vector  Phone_address;
    //Vector  Phone_remarks;
    String  cur_phoneNum;
    String  cur_phoneName;
    String  cur_phoneAddress;
    DefaultTableModel   dtm_phone;
    String  file_dir   = "Contacts";
    String  file_path = "Contacts\\danhba.thsv";
    public danh_ba_dien_thoai() {
        Phone_address   = new Vector();
        Phone_number    = new Vector();
        Phone_name      = new Vector();
//        Phone_remarks   = new Vector();
        initComponents();
        initial_contacts();
        intial_Phone_table();
    }

    public final void intial_Phone_table()
    {
        table_phone.getTableHeader().setFont(new Font("Times New Roman", Font.PLAIN, 18));
        TableColumn column = null;
        column = table_phone.getColumnModel().getColumn(0);
        column.setPreferredWidth(150);
        column = table_phone.getColumnModel().getColumn(1);
        column.setPreferredWidth(150);
        column = table_phone.getColumnModel().getColumn(2);
        column.setPreferredWidth(301);
    }

    public int initial_contacts()
    {
        int return_val = 0;
        String strLine;
        String[] idata ;
        try{
            FileInputStream in = new FileInputStream(file_path);
            try (BufferedReader bufffile = new BufferedReader(new InputStreamReader(in, "UTF8"))) 
            {
                strLine = bufffile.readLine();
                while (strLine != null)   {
                    idata = strLine.split("<#>");
                    Phone_number.add(idata[0]);
                    Phone_name.add(idata[1]);
                    Phone_address.add(idata[2]);
                    strLine = bufffile.readLine();
                }
                dtm_phone = (DefaultTableModel) table_phone.getModel();
                clear_table();
                display_phone();
            }
            in.close();
        }
        catch (Exception e){//Catch exception if any
            show_info("Không tìm thấy file danh bạ", Color.red);
        }
        return return_val;
    }
    public void clear_table()
    {
        dtm_phone.getDataVector().removeAllElements();
        dtm_phone.fireTableDataChanged();
    }
    public void display_phone()
    {
        int size = Phone_number.size();
        System.out.println("phone size: " + size);
        for(int i = 0; i < size; i++)
        {
            Vector data = new Vector();
            data.add(Phone_number.get(i));
            data.add(Phone_name.get(i));
            data.add(Phone_address.get(i));
            dtm_phone.addRow(data);
            System.out.println("addrow " + i);
        }
    }
    public void add_row2table(int i)
    {
        Vector data = new Vector();
        data.add(Phone_number.get(i));
        data.add(Phone_name.get(i));
        data.add(Phone_address.get(i));
        dtm_phone.addRow(data);
    }
    public void add2Phonelist()
    {
        Phone_number.add(cur_phoneNum);
        Phone_name.add(cur_phoneName);
        Phone_address.add(cur_phoneAddress);
    }
    public void update2Phonelist(int i)
    {
        Phone_number.set(i,cur_phoneNum);
        Phone_name.set(i,cur_phoneName);
        Phone_address.set(i,cur_phoneAddress);
    }
    public void remove_itemPhonelist(int i)
    {
        Phone_number.removeElementAt(i);
        Phone_name.removeElementAt(i);
        Phone_address.removeElementAt(i);
    }
    public void show_info(String info, Color icolor)
    {
        jtext_thong_bao.setForeground(icolor);
        jtext_thong_bao.setText(info);
    }
    public Vector search_phone( Vector iVector, String iString)
    {
        Vector return_val = new Vector();
        int size = iVector.size();
        if(!iString.isEmpty())
        {
            for(int i = 0; i < size; i++)
            {
                if(iVector.get(i).toString().contains(iString))
                {
                    return_val.add(i);
                }
            }
        }
        return return_val;
    }
    public void display_result(Vector iVector)
    {
        int size  = iVector.size();
        int index = 0;
        if(size > 0)
        {
            clear_table();
            for(int i = 0; i < size; i++)
            {
                index = Integer.parseInt(iVector.get(i).toString());
                add_row2table(index);
            }
        }
    }
    public int check_phone_number()
    {
        int return_val = -1;
        String phone_num = jtext_phone_number.getText();
        if(!phone_num.isEmpty())
        {
            try
            {
                Long.parseLong(phone_num);
                return_val = seach_phone_contacts(phone_num);
                if(return_val != -1)
                {
                    display_details_jtext(return_val);
                }
                return_val = 0;
            }
            catch(NumberFormatException e)
            {
                jtext_thong_bao.setText("Nhập sai lại số ĐT: " + phone_num);
            }
        }
        return return_val;
    }
    
    public int seach_phone_contacts(String SPhone)
    {
        int return_val = -1;
        int contacts_size = Phone_number.size();
        if(!SPhone.isEmpty())
        {
            for(int i = 0; i < contacts_size; i++)
            {
                if(SPhone.trim().equals(Phone_number.get(i).toString().trim()))
                {
                    return_val = i;
                    break;
                }
            }
        }
        return return_val;
    }
         
    public Vector search_name_contacts(String Sname)
    {
        Vector return_val = new Vector();
        int contacts_size = Phone_number.size();
        if(!Sname.isEmpty())
        {
            for(int i = 0; i < contacts_size; i++)
            {
                if(Phone_number.get(i).toString().trim().contains(Sname))
                {
                    return_val.add(i);
                }
            }
        }
        return return_val;        
    }
        
    public Vector search_address_contacts(String Saddress)
    {
        Vector return_val = new Vector();
        int contacts_size = Phone_number.size();
        if(!Saddress.isEmpty())
        {
            for(int i = 0; i < contacts_size; i++)
            {
                if(Phone_address.get(i).toString().trim().contains(Saddress))
                {
                    return_val.add(i);
                }
            }
        }
        return return_val;        
    }
    
    public void display_details_jtext(int index)
    {
        jtext_phone_number.setText(Phone_number.get(index).toString());
        jtext_phone_name.setText(Phone_name.get(index).toString());
        jtext_address.setText(Phone_address.get(index).toString());
    }
    public void Update_contact(int i)
    {
        
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        botton_add_edit_contacts = new javax.swing.JButton();
        jtext_phone_number = new javax.swing.JTextField();
        jtext_thong_bao = new javax.swing.JLabel();
        jtext_phone_name = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_phone = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        botton_display_contacts = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtext_address = new javax.swing.JTextArea();
        botton_delete_contacts = new javax.swing.JButton();

        jButton1.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Danh bạ điện thoại");

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 102));
        jLabel1.setText("Số đt: ");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 102));
        jLabel2.setText("Tên: ");

        botton_add_edit_contacts.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        botton_add_edit_contacts.setForeground(new java.awt.Color(0, 0, 102));
        botton_add_edit_contacts.setText("Thêm/sửa");
        botton_add_edit_contacts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botton_add_edit_contactsActionPerformed(evt);
            }
        });

        jtext_phone_number.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jtext_phone_number.setForeground(new java.awt.Color(0, 0, 102));
        jtext_phone_number.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtext_phone_numberMouseClicked(evt);
            }
        });
        jtext_phone_number.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtext_phone_numberActionPerformed(evt);
            }
        });
        jtext_phone_number.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtext_phone_numberKeyReleased(evt);
            }
        });

        jtext_thong_bao.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jtext_thong_bao.setForeground(new java.awt.Color(0, 0, 102));
        jtext_thong_bao.setText("Thông báo:");

        jtext_phone_name.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jtext_phone_name.setForeground(new java.awt.Color(0, 0, 102));
        jtext_phone_name.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtext_phone_nameMouseClicked(evt);
            }
        });
        jtext_phone_name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtext_phone_nameActionPerformed(evt);
            }
        });
        jtext_phone_name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtext_phone_nameKeyReleased(evt);
            }
        });

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh bạ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 0, 18), new java.awt.Color(0, 0, 102))); // NOI18N

        table_phone.setAutoCreateRowSorter(true);
        table_phone.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        table_phone.setForeground(new java.awt.Color(0, 0, 102));
        table_phone.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Số ĐT", "Tên", "Địa chỉ"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_phone.setRowHeight(26);
        table_phone.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_phoneMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(table_phone);

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 102));
        jLabel4.setText("Ghi chú :");

        botton_display_contacts.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        botton_display_contacts.setForeground(new java.awt.Color(0, 0, 102));
        botton_display_contacts.setText("Danh bạ");
        botton_display_contacts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botton_display_contactsActionPerformed(evt);
            }
        });

        jtext_address.setColumns(20);
        jtext_address.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jtext_address.setLineWrap(true);
        jtext_address.setRows(5);
        jtext_address.setWrapStyleWord(true);
        jtext_address.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtext_addressMouseClicked(evt);
            }
        });
        jtext_address.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtext_addressKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jtext_address);

        botton_delete_contacts.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        botton_delete_contacts.setForeground(new java.awt.Color(0, 0, 102));
        botton_delete_contacts.setText("Xóa");
        botton_delete_contacts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botton_delete_contactsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jtext_thong_bao, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(botton_delete_contacts, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jtext_phone_number)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jtext_phone_name, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(botton_add_edit_contacts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(botton_display_contacts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtext_phone_number, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jtext_phone_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(botton_add_edit_contacts)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(botton_display_contacts))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtext_thong_bao)
                    .addComponent(botton_delete_contacts))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel1, jLabel2, jtext_phone_name, jtext_phone_number, jtext_thong_bao});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botton_add_edit_contactsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botton_add_edit_contactsActionPerformed
        // TODO add your handling code here:
        add_edit_phone();
        clear_table();
        display_phone();
    }//GEN-LAST:event_botton_add_edit_contactsActionPerformed
    public void add_edit_phone()
    {
        int return_val =get_check_info();
        if(0 ==return_val && !jtext_phone_name.getText().trim().isEmpty())
        {
            //seach in contact
            return_val = seach_phone_contacts(jtext_phone_number.getText().trim());
            if(return_val != -1)
            {
                int i = show_message("Số ĐT này có trong danh bạ\nBấm \"Có\" để thay đổi!\n");
                if(i == 0)  //update
                {
                    update2Phonelist(return_val);
                    writeContacts2data();
                    show_info("đã thay đổi thông tin! " + cur_phoneNum, Color.BLUE);
                }
                else
                {
                    show_info("Không thay đổi!", Color.BLUE);
                }
            }
            else    //add new
            {
                add_contacts();
                show_info("Đã thêm vào danh bạ: " + cur_phoneNum, Color.blue);
            }
        }
        else 
        {
            show_info("[Không thành công]" + jtext_thong_bao.getText(), Color.red);
        }
    }
    public int get_check_info()
    {
        int return_val = 0;
        cur_phoneName   = jtext_phone_name.getText().trim();
        cur_phoneNum    = jtext_phone_number.getText().trim();
        cur_phoneAddress= jtext_address.getText().trim();
        cur_phoneAddress = cur_phoneAddress.replaceAll("\n", " ");
        if(cur_phoneNum.isEmpty())
        {
            jtext_thong_bao.setText("Không có số đt!");
            return_val = -1;
        }
        else if(cur_phoneName.isEmpty())
        {
            jtext_thong_bao.setText("Không có tên!");
            return_val = -2;
        }
        else if(cur_phoneAddress.isEmpty())
        {
            jtext_thong_bao.setText("Không có địa chỉ!");
            return_val = -3;
        }
        
        if(cur_phoneNum.length() < 10 || cur_phoneNum.length() >= 12)
        {
            jtext_thong_bao.setText("Số điện thoại sai!");
            return_val = -4;
        }
        return return_val;
    }
    public void add_contacts()
    {
        int return_val = get_check_info();
        System.out.println("return val: " + return_val);
        if(return_val == 0)
        {
            try
                {
                    // Create file 
                    File Fdir = new File(file_dir);
                    boolean  a = Fdir.mkdirs(); 
                    if(a || Fdir.isDirectory())
                    {
                        FileOutputStream fos = new FileOutputStream(file_path,true);
                        Writer out = new OutputStreamWriter(fos, "UTF8");
                        {
                            out.write(cur_phoneNum + "<#>" + cur_phoneName + 
                                    "<#>" + cur_phoneAddress + "\n");
                            add2Phonelist();
                            out.close();
                            fos.close();
                        }
                        
                    }
                }
                catch (Exception e){//Catch exception if any
                        System.err.println("Error: " + e.getMessage() + " add_contacts" );
                }
        }
        
    }
    public void writeContacts2data()
    {
        int phone_size = Phone_number.size();
        try
        {
            // Create file 
            File Fdir = new File(file_dir);
            boolean  a = Fdir.mkdirs(); 
            if(a || Fdir.isDirectory())
            {
                FileOutputStream fos = new FileOutputStream(file_path);
                Writer out = new OutputStreamWriter(fos, "UTF8");
                for(int i = 0; i < phone_size; i++)
                {
                    out.write(Phone_number.get(i) + "<#>" + Phone_name.get(i) + 
                        "<#>" + Phone_address.get(i) + "\n");
                }
                out.close();
            }
        }
        catch (Exception e){//Catch exception if any
                System.err.println("Error: writeContacts2data" + e.getMessage());
        }
    }
    public void add_new_phone_number()
    {
        int phone_idx = Phone_address.size();
        if(phone_idx > 0)
        {
            try
            {
                FileWriter fstream = new FileWriter(file_path ,true);
                try (BufferedWriter out = new BufferedWriter(fstream)) {
                    out.write(Phone_number.get(phone_idx -1) + "<#>" +
                              Phone_name.get(phone_idx -1) + "<#>" +
                            Phone_address.get(phone_idx -1) + "\n");
                            
                }
            }
            catch (Exception e){//Catch exception if any
                    show_info("ERROR! write file", Color.RED);
            }
        }
    }
    
    public void  update_phone_info(int index)
    {
        try{
            FileReader file1 = new FileReader(file_path);
            BufferedReader f = new BufferedReader(file1);
            FileWriter file2 = new FileWriter(file_path,true);
            BufferedWriter file3 = new BufferedWriter(file2);
            String temp = null;
            int i =0 ;
            while((temp=f.readLine()) !=null)
            {
                i++;
                if(i== index)
                {
                        file3.write(Phone_address.get(i) + "<#>" +
                              Phone_name.get(i) + "<#>" +
                            Phone_address.get(i) + "\n");
                        file3.close();
                        break;
                }
            }
        }
        catch(Exception e)
        {
            show_info("Lỗi", Color.RED);
        }
    }
    public void set_vector_data()
    {
        Phone_number.add(jtext_phone_number.getText().trim());
        Phone_name.add(jtext_phone_name.getText().trim());
        Phone_address.add(jtext_address.getText().trim());
    }
    public void update_vector_data(int i)
    {
        if(0 <= i)
        {
            Phone_number.set(i,jtext_phone_number.getText().trim());
            Phone_name.set(i,jtext_phone_name.getText().trim());
            Phone_address.set(i,jtext_address.getText().trim());
        }
    }
    public int show_message(String imessage)
     {
         int n = JOptionPane.showOptionDialog( null,
            imessage,
            "Tạp hóa Sáu Vân",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new String[]{"Có", "Không"}, "default");
         return n;
     }
     
    private void jtext_phone_nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtext_phone_nameActionPerformed
        // TODO add your handling code here:
        jtext_address.grabFocus();
    }//GEN-LAST:event_jtext_phone_nameActionPerformed

    private void jtext_phone_numberKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtext_phone_numberKeyReleased
        // TODO add your handling code here:
        check_phone_number();
        cur_phoneNum = jtext_phone_number.getText().trim();
        Vector result_val = search_phone(Phone_number, cur_phoneNum);
        display_result(result_val);
        
    }//GEN-LAST:event_jtext_phone_numberKeyReleased
    
    private void jtext_phone_numberMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtext_phone_numberMouseClicked
        // TODO add your handling code here:
        jtext_phone_number.setText(null);
    }//GEN-LAST:event_jtext_phone_numberMouseClicked

    private void jtext_phone_nameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtext_phone_nameMouseClicked
        // TODO add your handling code here:
        jtext_phone_name.setText(null);
    }//GEN-LAST:event_jtext_phone_nameMouseClicked

    private void botton_display_contactsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botton_display_contactsActionPerformed
        // TODO add your handling code here:
        clear_table();
        display_phone();
    }//GEN-LAST:event_botton_display_contactsActionPerformed

    private void jtext_phone_numberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtext_phone_numberActionPerformed
        // TODO add your handling code here:
        jtext_phone_name.grabFocus();
    }//GEN-LAST:event_jtext_phone_numberActionPerformed

    private void table_phoneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_phoneMouseClicked
        // TODO add your handling code here:
        int row_select      = table_phone.getSelectedRow();
        jtext_phone_number.setText(table_phone.getValueAt(row_select, 0).toString());
        jtext_phone_name.setText(table_phone.getValueAt(row_select, 1).toString());
        jtext_address.setText(table_phone.getValueAt(row_select, 2).toString());
        
    }//GEN-LAST:event_table_phoneMouseClicked

    private void jtext_phone_nameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtext_phone_nameKeyReleased
        // TODO add your handling code here:
        cur_phoneName = jtext_phone_name.getText().trim();
        Vector result_val = search_phone(Phone_name, cur_phoneName);
        //clear table
        display_result(result_val);
    }//GEN-LAST:event_jtext_phone_nameKeyReleased

    private void jtext_addressKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtext_addressKeyReleased
        // TODO add your handling code here:
        cur_phoneAddress = jtext_address.getText().trim();
        Vector result_val = search_phone(Phone_address, cur_phoneAddress);
        display_result(result_val);
    }//GEN-LAST:event_jtext_addressKeyReleased

    private void jtext_addressMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtext_addressMouseClicked
        // TODO add your handling code here:
        jtext_address.setText("");
    }//GEN-LAST:event_jtext_addressMouseClicked

    private void botton_delete_contactsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botton_delete_contactsActionPerformed
        int return_val = get_check_info();
        if(return_val == 0)
        {
            return_val = seach_phone_contacts(cur_phoneNum);
            if(return_val != -1) //found
            {
                int i = show_message("Xác nhận xóa danh bạ!\n Số đt: " + cur_phoneNum +
                        "\nTên: " + cur_phoneName + "\nThông tin liên quan:\n" + cur_phoneAddress);
                if(i == 0)
                {
                    remove_itemPhonelist(return_val);
                    writeContacts2data();
                    show_info("[Đã xóa] " + cur_phoneNum + ", " + cur_phoneName, Color.BLUE);
                    clear_table();
                    display_phone();
                }
                else
                {
                    show_info("[Hủy xóa] " + cur_phoneNum + ", " + cur_phoneName, Color.BLUE);
                }
            }
            else
            {
                show_info("[Chưa xóa] Không tìm thấy: " + cur_phoneNum, Color.BLUE);
            }
        }
        else
        {
            show_info("[Chưa xóa]" + jtext_thong_bao.getText() , Color.RED);
        }
    }//GEN-LAST:event_botton_delete_contactsActionPerformed

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
            java.util.logging.Logger.getLogger(danh_ba_dien_thoai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(danh_ba_dien_thoai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(danh_ba_dien_thoai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(danh_ba_dien_thoai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new danh_ba_dien_thoai().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botton_add_edit_contacts;
    private javax.swing.JButton botton_delete_contacts;
    private javax.swing.JButton botton_display_contacts;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jtext_address;
    private javax.swing.JTextField jtext_phone_name;
    private javax.swing.JTextField jtext_phone_number;
    private javax.swing.JLabel jtext_thong_bao;
    private javax.swing.JTable table_phone;
    // End of variables declaration//GEN-END:variables
}

