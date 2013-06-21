/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sale;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import javax.imageio.ImageIO;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
/**
 *
 * @author Binh
 */
public final class Main_form extends javax.swing.JFrame {

    /**
     * Creates new form Main_form
     */
    Goods_price SauVan = new Goods_price();
    public Bill_Printer in_hoa_don = new Bill_Printer();
    public Phone_contracts danhba    = new Phone_contracts();
    
    public Vector<String> Paid_MaSP;
    public Vector<String> Paid_TenSP;
    public Vector<Double> Paid_GiaSSP;
    public Vector<Double> Paid_GiaLSP;
    public Vector<Double> Paid_SoLuongSP;
    public Vector<Double> Paid_TongGiaSP;
    
    public Vector<String> search_list;
    public int            hand_offset =1;
    private static DecimalFormat moneyFormat;
    private static DecimalFormat slFormat0, slFormat1, slFormat2;
    private double      Money_count_Items   = 0;
    private Double      Money_total_bill    = 0.0;
    private Double      Money_custumer      = 0.0;
    private Double      Money_return        = 0.0;
    private Vector<String> New_MaSP;
    
    private int return_result = 0;
    private int diffFloatPoint = 0;
    private String  Consumer_name;
    private String   file_path;
    private DefaultTableModel dm_hoa_don;
    private boolean     hd_inMethodClean = false;
    private DefaultTableModel dm_info_SP;
    private DefaultComboBoxModel Cbm_history;
    private Vector  curSP;
    private String  curMaSP;
    private Boolean Updated_mode;
    private Boolean giaS_mode;
    //private Boolean Process_pay_mode = false;
    private History_info history_tracking;
    private static String datarepository    = "data";
    private static String billConfigFile    = datarepository + "\\configuration.thsv";
    private static String saveBillFile      = datarepository + "\\SaveBill.thsv";
    private        String printer_folder    = "Printer";
    private        String barcode_printer   = "Print_barcode";
    private static String owner_name = "";
    private static String phone_number = "";
    private static int MAX_NAME_CHAR    = 1;
    private static int saveBillBound    = 5;
    private static int saveBillCount    = 0;
    private        int saveBillIdxNoti  = 1;
    private String sDate_time;
    private double dno_cu =0.0;
    private static File f;
    private static FileChannel channel;
    private static FileLock lock;
    
    public Main_form() {
    
        check_program();
        this.Paid_MaSP       = new Vector<>();
        this.Paid_TenSP      = new Vector<>();
        this.Paid_GiaSSP     = new Vector<>();
        this.Paid_GiaLSP     = new Vector<>();
        this.Paid_SoLuongSP  = new Vector<>();
        this.Paid_TongGiaSP  = new Vector<>();
        this.New_MaSP   = new Vector<>();
        this.curSP      = new Vector();
        this.search_list     = new Vector<>();
        history_tracking    = new History_info();
        initComponents();
        init_eviroment();
        setPayTableColumnSize();
        setInfoTableColumnSize();
        display_date();
        get_bill_configure();
        thongbao_text("Tạp hóa Sáu Vân", Color.BLUE);
   }
    public static void check_program()
    {
        try {
            f = new File("RingOnRequest.lock");
            // Check if the lock exist
            if (f.exists()) {
                // if exist try to delete it
                f.delete();
            }
            // Try to get the lock
            channel = new RandomAccessFile(f, "rw").getChannel();
            lock = channel.tryLock();
            if(lock == null)
            {
                // File is lock by other application
                channel.close();
                JOptionPane.showOptionDialog( null,
                "Chỉ chạy được tối đa 1 chương trình",
                "Tạp hóa Sáu Vân",
                JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE,
                null, new String[]{"OK"}, "default");
                throw new RuntimeException("Only 1 instance of MyApp can run.");
            }
        }
        catch(IOException e)
        {
            JOptionPane.showOptionDialog( null,
                "Chỉ chạy được tối đa 1 chương trình",
                "Tạp hóa Sáu Vân",
                JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE,
                null, new String[]{"OK"}, "default");
            throw new RuntimeException("Could not start process.", e);
        }
    }
    public static void get_bill_configure()
    {
        try{
            FileInputStream in = new FileInputStream(billConfigFile);
            try (BufferedReader bufffile = new BufferedReader(new InputStreamReader(in, "UTF8"))) 
            {
                String strLine;
                strLine = bufffile.readLine();
                while (strLine != null)   {
                    if(strLine.substring(0, 3).equals("=01"))
                    {
                        phone_number = strLine.substring(3);
                    }
                    else if(strLine.substring(0, 3).endsWith("=00"))
                    {
                        owner_name = strLine.substring(3);
                    }
                    else if(strLine.substring(0, 3).endsWith("=09"))
                    {
                        MAX_NAME_CHAR = Integer.parseInt(strLine.substring(3));
                    }
                    strLine = bufffile.readLine();
                }
            }
            in.close();
        }catch (Exception e){//Catch exception if any
            phone_number  = "(083)592.33.79";
            owner_name    = "Tạp Hóa SÁU VÂN";
            MAX_NAME_CHAR = 20;
        }
    }
    public static void unlockFile() {
        // release and delete file lock
        try {
            if(lock != null) {
                lock.release();
                channel.close();
                f.delete();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    public final void init_eviroment()
    {
        botton_update_database.setEnabled(false);
        dm_hoa_don = (DefaultTableModel)bang_HoaDon.getModel();
        dm_info_SP = (DefaultTableModel)bang_tim_info_SP.getModel();
        Cbm_history = (DefaultComboBoxModel) ComboBox_history.getModel();
        Updated_mode = false;
        giaS_mode    = false;
        botton_add_prod2Paylist.setEnabled(false);
        normal_mode(false);
        giaS_mode = select_price.isSelected();
        select_price.setText("Bán giá lẻ");
        moneyFormat = new DecimalFormat("#,##0.0");
        slFormat0   = new DecimalFormat("#0");
        slFormat1   = new DecimalFormat("#0.0");
        slFormat2   = new DecimalFormat("#0.00");
        setframeicon();
    }
    public void setframeicon(){
    try{
        InputStream imgStream = this.getClass().getResourceAsStream("/sale/img/Icon_barcode.png");
        BufferedImage bi = ImageIO.read(imgStream);
        ImageIcon myImg = new ImageIcon(bi);
        this.setIconImage(myImg.getImage());
        }catch(Exception e){
            System.out.println(e);
        }   
    }
    public void get_date()
    {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        sDate_time = dateFormat.format(date).toString();
    }
    public void display_date()
    {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        sDate_time = dateFormat.format(date).toString();
        date_time.setText(sDate_time.substring(0,10));

        String  file_date   = dateFormat.format(date).toString();
        String[] file_info = file_date.split("/");

        File File_path = new File(file_info[2]);
        if(!File_path.exists())
        {
            File_path.mkdirs();
        }
        
        if(File_path.exists())
        {
            String file_BK_name = file_info[2] + "\\\\" +file_info[1]+"_" +
                    file_info[0] + "goods_info.xls"; 
            File bk_file_path = new File(file_BK_name);
            if(!bk_file_path.exists()) //copy if not exist
            {
                try {
                    
                    String line ="cmd /C copy goods_info.xls " +file_BK_name;
                    System.out.println(line);
                    Process p = Runtime.getRuntime().exec(line);
                    p.waitFor();
                } catch (            IOException | InterruptedException ex) {
                    System.out.println("ERROR! COPY");
                }
            }
        }
    }
    
    public final void setPayTableColumnSize()
    {
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment( JLabel.RIGHT );
        bang_HoaDon.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 16));
        TableColumn column = null;
        column = bang_HoaDon.getColumnModel().getColumn(0);
        column.setPreferredWidth(35);
        column.setCellRenderer(rightRenderer) ;
        column = bang_HoaDon.getColumnModel().getColumn(1);
        column.setPreferredWidth(175);
        column = bang_HoaDon.getColumnModel().getColumn(2);
        column.setPreferredWidth(495);
        column = bang_HoaDon.getColumnModel().getColumn(3);
        column.setPreferredWidth(117);
        column.setCellRenderer(rightRenderer) ;
        column = bang_HoaDon.getColumnModel().getColumn(4);
        column.setPreferredWidth(117);
        column.setCellRenderer(rightRenderer) ;
        column = bang_HoaDon.getColumnModel().getColumn(5);
        column.setPreferredWidth(117);
        column.setCellRenderer(rightRenderer) ;
        column = bang_HoaDon.getColumnModel().getColumn(6);
        column.setPreferredWidth(140);
        column.setCellRenderer(rightRenderer) ;
        column = bang_HoaDon.getColumnModel().getColumn(7);
        column.setPreferredWidth(70);
        //bang_HoaDon.moveColumn(4, 5);
    }
    public final void setInfoTableColumnSize()
    {
        bang_tim_info_SP.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 14));
        TableColumn column = null;
        column = bang_tim_info_SP.getColumnModel().getColumn(0);
        column.setPreferredWidth(10);
        column = bang_tim_info_SP.getColumnModel().getColumn(1);
        column.setPreferredWidth(250);
        column = bang_tim_info_SP.getColumnModel().getColumn(2);
        column.setPreferredWidth(20);
        column = bang_tim_info_SP.getColumnModel().getColumn(3);
        column.setPreferredWidth(20);
        column = bang_tim_info_SP.getColumnModel().getColumn(4);
        column.setPreferredWidth(20);
        column = bang_tim_info_SP.getColumnModel().getColumn(5);
        column.setPreferredWidth(5);
    }
    public int search_onpaylist(String maSP)
    {
        int zise_list = Paid_MaSP.size();
        for(int i = 0; i < zise_list; i++)
        {
            if(Paid_MaSP.get(i).equals(maSP)){
                return i;
            }
        }
        return -1;
    }
    public final void addtopay(int i)
    {
        System.out.println("ADD TO PAY LIST WHITH INDEX" + i);
        Vector     CurSP    = new Vector();
        CurSP   = SauVan.get_sp(i);
        return_result = search_onpaylist(CurSP.elementAt(0).toString());
        //not found in buying list
        Double temp_Paid_soluong;
        int index_new_one;
        if(return_result == -1) //add new one into pay list
        {
            thongbao_text("[Thêm] sản phẩm : " + CurSP.get(1).toString(), Color.BLUE);
            Paid_MaSP.add(CurSP.get(0).toString());
            Paid_TenSP.add(CurSP.get(1).toString());
            Paid_GiaSSP.add(Double.parseDouble(CurSP.get(2).toString()));
            Paid_GiaLSP.add(Double.parseDouble(CurSP.get(3).toString()));
            Paid_SoLuongSP.add(Double.parseDouble(CurSP.get(4).toString()));
            
            //calculate price for this item then add to Paid_TongGiaSP vector
            index_new_one = Paid_MaSP.size();
            calculate_item_paid_row(index_new_one -1);
            
            //add row into table
            add_SP_row();
            add_Paid_row();
            
         }
         else //already in pay list
         {
             thongbao_text("[đã có trong giỏ hàng] sản phẩm : " + CurSP.get(1).toString(), Color.BLUE);
             
             temp_Paid_soluong  = Paid_SoLuongSP.get(return_result) + 1;
             System.out.println("debug 11 " + temp_Paid_soluong + "index" + return_result);
             Paid_SoLuongSP.set(return_result, temp_Paid_soluong);
             System.out.println("debug 12 " + Paid_SoLuongSP.get(return_result));
             display_text_paylist();
             calculate_item_paid_row(return_result);
             update_item_row(return_result);
             //Paid_TongGiaSP.set(return_result, temp_Paid_soluong * temp_paid_gia);
             
             // update table
             //System.out.println("count: M" + Paid_MaSP.size() + "-" + Paid_TenSP.size() + "-"+
             //        Paid_GiaSSP.size() + "-" + Paid_SoLuongSP.size() + "-" + Paid_TongGiaSP.size());
             //bang_HoaDon.getModel().setValueAt(Paid_SoLuongSP.get(return_result), return_result, 5);
             //bang_HoaDon.getModel().setValueAt(Paid_TongGiaSP.get(return_result), return_result, 6);
             update_total_Paid_row();
         }
        dm_hoa_don.fireTableDataChanged();

     }
     
    public void calculate_item_paid_row(int i)
    {
        double temp_tong ;
        if(i >=0)
        {
            giaS_mode = select_price.isSelected();
            if(i == Paid_TongGiaSP.size()) //add mode
            {
                if(giaS_mode)
               {
                   temp_tong = Paid_SoLuongSP.get(i)*Paid_GiaSSP.get(i);
               }
               else
               {
                   temp_tong = Paid_SoLuongSP.get(i)*Paid_GiaLSP.get(i);
               }
                Paid_TongGiaSP.add(temp_tong);
            }
            else
            {
                if(giaS_mode)
                {
                    temp_tong = Paid_SoLuongSP.get(i)*Paid_GiaSSP.get(i);
                }
                else
                {
                    temp_tong = Paid_SoLuongSP.get(i)*Paid_GiaLSP.get(i);
                    
                }
                Paid_TongGiaSP.set(i, temp_tong);
            }
        }
        else
        {
            thongbao_text("CODE ERROR! :calculate_items_paid_row:" + i, Color.RED);
        }
    }
    public void update_item_row(int i)
    {
        bang_HoaDon.getModel().setValueAt(Paid_GiaSSP.get(i), i, 3);
        bang_HoaDon.getModel().setValueAt(Paid_GiaLSP.get(i), i, 4);
        bang_HoaDon.getModel().setValueAt(Paid_SoLuongSP.get(i), i, 5);
        bang_HoaDon.getModel().setValueAt(Paid_TongGiaSP.get(i), i, 6);        
    }
    public void clear_table(DefaultTableModel tableModel)
    {
         tableModel.getDataVector().removeAllElements();
         tableModel.fireTableDataChanged(); // notifies the JTable that the model has changed
     }

    public void add_SP_row()
    {
         int rowcount = bang_HoaDon.getRowCount();
         if (rowcount != 0)
         {
             System.out.println("delete last row");
            dm_hoa_don.removeRow(rowcount-1);
         }
         int last_row = Paid_MaSP.size() - 1;
         
         Vector data_row = new Vector();
         data_row.add(last_row + 1);
         data_row.add(Paid_MaSP.get(last_row));
         data_row.add(Paid_TenSP.get(last_row));
         data_row.add(Paid_GiaSSP.get(last_row));
         data_row.add(Paid_GiaLSP.get(last_row));
         data_row.add(Paid_SoLuongSP.get(last_row));
         data_row.add(Paid_TongGiaSP.get(last_row));
         data_row.add(Boolean.TRUE);
         dm_hoa_don.addRow(data_row);
     }
     
    public void add_Paid_row()
    {

         Vector Paid    = new Vector();
         calculate_total_price();
         Paid.add(null);
         Paid.add(null);
         Paid.add(null);
         Paid.add(null);
         Paid.add(null);
         Paid.add(Money_count_Items);
         Paid.add(Money_total_bill);
         dm_hoa_don.addRow(Paid);
     }
    public void calculate_total_price()
    {
         int sizerow = Paid_MaSP.size();
         Money_count_Items = 0;
         Money_total_bill  = 0.0;
         for(int i = 0; i < sizerow; i++)
         {
             if((boolean)bang_HoaDon.getValueAt(i, 7))
             {
                Money_count_Items += Paid_SoLuongSP.get(i);
                Money_total_bill += Paid_TongGiaSP.get(i);
             }
         }
    }
    public void update_total_Paid_row()
    {
        int sizerow = Paid_MaSP.size();
        calculate_total_price();
        bang_HoaDon.getModel().setValueAt(Money_count_Items, sizerow, 5);
        bang_HoaDon.getModel().setValueAt(Money_total_bill, sizerow, 6);
     }
    
    public void add_info_SP2BTK(String iMaSP)
    {
         int new_MaSP_size = New_MaSP.size();
         int i;
         int    isoluong_update;
         Object osoluong_update;
         return_result = -1;
         Vector data_row = new Vector();
         for(i = 0; i < new_MaSP_size; i++)
         {
             if(New_MaSP.get(i).equals(iMaSP))
             {
                 return_result = i;
                 break;
             }
         }
         
         if(return_result == -1)
         {
            New_MaSP.add(iMaSP);
            data_row.add(iMaSP);
            data_row.add("Ten");
            data_row.add("0");
            data_row.add("0");
            data_row.add("1");
            data_row.add(Boolean.TRUE);
            dm_info_SP.addRow(data_row);
         }
         else       //already have one
         {
             
             osoluong_update = bang_tim_info_SP.getModel().getValueAt(i, 4);
             isoluong_update = Integer.parseInt(osoluong_update.toString()) + 1;
             bang_tim_info_SP.setValueAt(isoluong_update, i, 4);
             thongbao_text("warning: Đã có trong bảng cập nhập!", Color.BLUE);
         }
     }
        
    public void update_info_SP2BTK(String updateMaSP)
    {
        int j =0, i = 0;
        int update_MaSP_size = New_MaSP.size();
        return_result = -1;
        Vector update_info_sp;
        thongbao_text("", Color.BLUE);
        for(j = 0; j < update_MaSP_size; j++)
        {
             if(New_MaSP.get(j).equals(updateMaSP))
             {
                 return_result = 0;
                 break;
             }
         }
        if(return_result != 0)
        {
             i = SauVan.tim_sp(updateMaSP);
            update_info_sp = SauVan.get_sp(i);
            update_info_sp.add(Boolean.TRUE);
            dm_info_SP.addRow(update_info_sp);
            New_MaSP.add(updateMaSP);
        }
         else
         {
             thongbao_text("Đã có trong danh sách: hàng " + i + 1, Color.RED);
         }
    }
    
    public void handle_Consumer_name()
    {
        //String chi = "Chị";
        //String chu = "Chú";
        //String co  = "Cô";
        if(Consumer_name.equals("")){
            Consumer_name = "Ẩn danh";
        }
        else if(Consumer_name.substring(0, 3).contains("chi")){
            //String replaceFirst = Consumer_name.replaceFirst("chi", chi);
        }
        else if(Consumer_name.substring(0, 3).contains("co")){
            //Consumer_name.substring(0, 3).replaceFirst("co", co);
        }
        else if(Consumer_name.substring(0, 3).contains("chu")){
           // Consumer_name.substring(0, 3).replaceFirst("chu", chu);
        }
        ten_khach_hang.setText("");
    }
    public void cal_price_items()
    {
        int paid_count = Paid_MaSP.size();
        double  temp_price;
        giaS_mode = select_price.isSelected();
        if(giaS_mode)
        {
            select_price.setText("Bán giá Sỹ");
            select_price.setToolTipText("GIÁ SỸ");
        }
        else
        {
            select_price.setText("Bán giá Lẻ");
            select_price.setToolTipText("GIÁ LẺ");
        }        
        if(paid_count > 0 )
        {
            for(int i =0; i< paid_count; i++)
            {
                if((boolean)bang_HoaDon.getValueAt(i, 7))
                {
                    if(giaS_mode)       //gia sy
                    {
                        temp_price = Paid_GiaSSP.get(i)*(Paid_SoLuongSP.get(i));
                    }
                    else
                    {
                        temp_price = Paid_GiaLSP.get(i)*(Paid_SoLuongSP.get(i));
                    }
                    Paid_TongGiaSP.set(i,temp_price);
                }
                bang_HoaDon.setValueAt(Paid_TongGiaSP.get(i), i, 6);
            }
            update_total_Paid_row();
        }
    }
    public int thong_bao_in()
    {
        String  sNotification = "";
        String  sPadding      = "...............................................";
        String  ten_sp        ="";
        int     items_count   = Paid_MaSP.size();
        for(int i = 0; i < items_count; i++)
        {
            ten_sp = Paid_TenSP.get(i) + sPadding + sPadding;
            ten_sp = ten_sp.substring(0,40).trim();
            sNotification += Paid_SoLuongSP.get(i) + " " + ten_sp +
                    " " + Paid_TongGiaSP.get(i) + "\n";
            if(i >= 25)
            {
                sNotification += "Không đủ không gian để hiển thị\n";
                break;
            }
        }
        sNotification += "-------------------------------------------------\n";
        sNotification += "tổng sl: " + Money_count_Items + ", tiền: " 
                + Money_total_bill + "\n";
        
        if(!No_cu.getText().isEmpty())
        {
            try
            {
                dno_cu = Double.parseDouble(No_cu.getText());
            }
            catch (NumberFormatException e)
            {
                thongbao_text("Nhập sai số nợ!", Color.RED);
                dno_cu = 0.0;
            }
        }else{
            dno_cu = 0.0;
        }
        double tong_gia = Money_total_bill + dno_cu;
        sNotification += "Nợ cũ: " + dno_cu + ", Tổng tiền: " + tong_gia+"\n";
        int n = JOptionPane.showOptionDialog( null,
            sNotification,
            "Xác nhận hóa đơn",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new String[]{"In", "Chỉnh lại"}, "default");
         return n;
    }
    public void pre_paid()
    {
        int i, count = 1;
        get_date();
        if(!Paid_MaSP.isEmpty() && Money_count_Items != 0)
        {
            normal_mode(true);
            ComboBox_history.setEnabled(false);
            Vector Final_paid_SelectSP = new Vector<>();
            for( i =0; i < Paid_MaSP.size(); i++)
            {
                Final_paid_SelectSP.add(bang_HoaDon.getValueAt(i, 7));
            }
            clear_table(dm_hoa_don);
            clear_table(dm_info_SP);

            for( i = 0; i < Paid_MaSP.size(); i++)
            {
                System.out.println(i + "--" + Final_paid_SelectSP.get(i));
                Vector data_row = new Vector();
                data_row.add(Paid_MaSP.get(i));
                data_row.add(Paid_TenSP.get(i));
                data_row.add(Paid_GiaSSP.get(i));
                data_row.add(Paid_GiaLSP.get(i));
                data_row.add(Paid_SoLuongSP.get(i));
                if((boolean)Final_paid_SelectSP.get(i)) //add row to pay table
                {
                    data_row.add(0,count++);
                    data_row.add(Paid_TongGiaSP.get(i));
                    data_row.add(Boolean.TRUE);
                    dm_hoa_don.addRow(data_row);
                }
                else    //add row to info table
                {
                    data_row.add(Boolean.TRUE);
                    New_MaSP.add(Paid_MaSP.get(i));
                    remove_element_paylist(i);
                    Final_paid_SelectSP.removeElementAt(i);
                    i--;
                    dm_info_SP.addRow(data_row);
                }
                display_text_paylist();
                dm_hoa_don.fireTableDataChanged();
            }
            add_Paid_row();
        }
        else
        {
            thongbao_text("Không có sản phẩm để thanh toán!", Color.BLUE);
        }
        
        field_price_other.setText("0.0");
        field_return_money.setText("0.0");
        barCode_grabFocus();
    }
    public int paid_function()
    {
        int return_val = -1;
        //int return_user = 0;
        //get name of Consumer
        Consumer_name = ten_khach_hang.getText();
        if(Consumer_name.equals(""))
        {
            Consumer_name ="..........";
        }
        dno_cu = 0.0;
        if(!No_cu.getText().isEmpty())
        {
            try
            {
                dno_cu = Double.parseDouble(No_cu.getText());
            }
            catch (NumberFormatException e)
            {
                thongbao_text("Nhập sai số nợ!", Color.RED);
                dno_cu = 0.0;
            }
        }
        cal_price_items();
        //handle_Consumer_name();
        try
        {
            Money_custumer = Double.parseDouble(field_price_other.getText().toString());
            Money_return = Money_custumer - Money_total_bill;
            write2datafile();
            return_val = 0;
        }
        catch (NumberFormatException e) {
            thongbao_text("Nhập sai số tiền lấy vào!", Color.RED);
            return_val = -1;
        }
        return return_val;
    }
    
    public int write2datafile()
    {
        int return_val = 0;
        int paid_count  = Paid_MaSP.size();
        String file_date           = sDate_time;
        String[] time_info = file_date.split(" ");
        String[] file_info = time_info[0].split("/");
        String   file_dir = file_info[2] +"\\" + file_info[1];
        if(paid_count != 0)
        {
            try
            {
                // Create file 
                File Fdir = new File(file_dir);
                boolean  a = Fdir.mkdirs(); 
                if(a || Fdir.isDirectory())
                {
                    file_path   = file_dir +"\\" + file_info[0]+".thsv";
                    ComboBox_history.addItem(time_info[1] + "<>" +Consumer_name);
                    FileOutputStream fos = new FileOutputStream(file_path,true);
                    Writer out = new OutputStreamWriter(fos, "UTF8");
                    {
                        out.write("-->" +file_date + "<>" +Consumer_name + "\n" );
                        
                        for(int i = 0; i < paid_count; i++)
                        {
                            out.write("---" + Paid_MaSP.get(i) + "<>" + 
                                    Paid_TenSP.get(i) + "<>" + Paid_SoLuongSP.get(i) 
                                    + "<>" +Paid_TongGiaSP.get(i)
                                    + "<>" +Paid_GiaSSP.get(i)
                                    + "<>" +Paid_GiaLSP.get(i)+ "\n");
                            SauVan.update_distribution(Paid_MaSP.get(i));
                        }
                            
                        out.write("==>"+ Money_count_Items +"<>" + 
                                Money_total_bill  + "<>" +Money_custumer + "<>"
                                + Money_return + "\n");
                        out.write("==-"+ dno_cu + "\n");
                        out.close();
                    }
                }
            }
             
            catch (Exception e){//Catch exception if any
                    System.err.println("Error: " + e.getMessage());
            }
        }
        return return_val;
    }
    public void normal_mode(Boolean ibool)
    {

        Label_other_price.setVisible(ibool);
        Label_other_price1.setVisible(ibool);
        field_price_other.setVisible(ibool);
        field_return_money.setVisible(ibool);
        botton_pay_print_bill.setVisible(ibool);
        field_price_other.setVisible(ibool);
        botton_update_mode.setEnabled(!ibool);
    }
    public void barCode_grabFocus()
    {
        field_barcode_maSP.setText("");
        field_barcode_maSP.grabFocus();
    }
    public void get_moneyformuser()
    {
        double return_money;
        thongbao_text("", Color.black);
        try {
                    
            double Price = Double.parseDouble(field_price_other.getText());
            return_money = Price - Money_total_bill;
            if(return_money < 0)
            {
                thongbao_text("Tiền đưa vào không đủ. thiếu " + return_money + "k.", Color.RED);
            }
                    
            field_return_money.setText(return_money + "k");
        } catch (NumberFormatException e) {
            thongbao_text("Nhập sai giá.", Color.red);
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

     public void Print_bill_process()
    {
        try {
            File Fdir = new File("Printer");
            boolean  a = Fdir.mkdirs(); 
            if(a || Fdir.isDirectory())
            {
                FileOutputStream fos = new FileOutputStream("Printer\\Printer_bill");
                try (Writer out = new OutputStreamWriter(fos, "UTF8")) {
                    //get format number of item row
                    diffFloatPoint  = diff_slFormat();
                    
                    String raw_data = Print_header2s();
                    out.write(raw_data);
                    raw_data = Print_contents2s();
                    out.write(raw_data);
                    raw_data = Print_money2s();
                    out.write(raw_data);
                    if(dno_cu > 0)
                    {
                        out.write("==-" + dno_cu + "\n");
                    }
                }
                in_hoa_don.print_handle(0);
            }
        } catch (IOException ex) {
            System.out.println("have no file Printer_bill");
        }
    }
     
     
     public String Print_header2s()
     {
         String sData_row;
         Consumer_name = ten_khach_hang.getText().trim();
         if(Consumer_name.isEmpty())
         {
             Consumer_name = ".........................";
         }
         sData_row = "Ngày: " + sDate_time + "\n";
         sData_row += owner_name + "\n" ;
         sData_row += "ĐT: " + phone_number +"\n";
         sData_row += "Khách hàng: " + Consumer_name + "\n";
         return sData_row;
     }
     public void   Print_barcode()
     {
         in_hoa_don.print_handle(1);  //1 mean print barcode
     }
     
     public int    get_info_barcode()
     {
         int return_val = -1;
         
         int iprint_size = bang_HoaDon.getRowCount();
        if(iprint_size>0)
        {
            pre_paid();
            normal_mode(false);
            return_val = 0;
            iprint_size = Paid_MaSP.size();
            try
            {
                // Create file 
                File Fdir = new File(printer_folder);
                boolean  a = Fdir.mkdirs(); 
                if(a || Fdir.isDirectory())
                {
                    FileOutputStream fos = new FileOutputStream(printer_folder + "\\" + barcode_printer);
                    Writer out = new OutputStreamWriter(fos, "UTF8");
                    {
                        for(int i = 0; i < iprint_size; i++)
                        {
                                out.write(Paid_MaSP.get(i) + "<<>>" + 
                                        Paid_TenSP.get(i) + "\n");
                        }
                        out.close();
                    }
                }
                
            } catch (FileNotFoundException ex) {
                thongbao_text("Print_barcode: FileNotFoundException", Color.red);
                return_val = -1;
            } catch (UnsupportedEncodingException ex) {
                return_val = -2;
                thongbao_text("Print_barcode: FileNotFoundException", Color.red);
            } catch (IOException ex) {
                return_val = -3;
                thongbao_text("Print_barcode: FileNotFoundException", Color.red);
            }
        }
        else
        {
            thongbao_text("Không có Barcode trong bảng!", Color.RED);
        }
        return return_val;
     }
     public String Print_contents2s()
     {
         String sData_row;
         int idata_size;
        sData_row = "Stt Tên sản phẩm             sl  Tiền(1000đ)\n"; 
        sData_row = sData_row + "-----------------------------------------------------------\n";
        idata_size = Paid_MaSP.size();
        for(int i = 0; i < idata_size; i++)
        {
            sData_row = sData_row + print_get_content(i);
        }
        sData_row = sData_row + "-----------------------------------------------------------\n";
        return  sData_row;
     }
     public String Print_money2s()
     {
         String sPrice = moneyFormat.format(Money_total_bill);
         String sSumitem = formatNumber(Money_count_Items, diffFloatPoint);
         String sMoney = "==>" + sSumitem + "<<>>"+ sPrice +"\n";
         return sMoney;
     }
     public String print_get_content(int i)
     {
         String print_row = null;
         String ten_sanpham  = Paid_TenSP.get(i).toString().trim();
         String soluong      = formatNumber(Paid_SoLuongSP.get(i),diffFloatPoint);
         String Thanh_tien   = moneyFormat.format(Paid_TongGiaSP.get(i));
         if(ten_sanpham.length() > MAX_NAME_CHAR)
         {
            ten_sanpham = ten_sanpham.substring(0, MAX_NAME_CHAR);
         }
         if(ten_sanpham.length() >=2)
         {
            ten_sanpham =  ten_sanpham.substring(0, 1).toUpperCase() +
                           ten_sanpham.substring(1).toLowerCase();
         }
         if(ten_sanpham.isEmpty())
         {
             ten_sanpham ="Lỗi: Tên ngắn quá!";
         }
         print_row = "--+" + ten_sanpham +"<<>>" + soluong + "<<>>" +Thanh_tien + "\n";
         return print_row;
     }
     
     public void in_hoa_don()
     {
         if(bang_HoaDon.getRowCount() != 0)
         {
            int return_val;
            pre_paid();
            return_val = thong_bao_in();
            if(return_val ==0)
            {
                return_val = paid_function();
            }
            if(return_val == 0)
            {
                Print_bill_process();
                clear_Paid_list();
                clear_Updated_list();
                clear_hand_list();
                clear_table(dm_hoa_don);
                clear_table(dm_info_SP);
                No_cu.setText("0.0");
                //get back enable some thiong
                normal_mode(false);
                ComboBox_history.setEnabled(true);
                thongbao_text("Đã thanh toán: " + Money_count_Items + " mặt hàng, Tổng giá "
                        + moneyFormat.format(Money_total_bill) + ", Lấy vào "
                        + Money_custumer + ", Trả lại " 
                        + moneyFormat.format(Money_return), Color.BLUE);
                ten_khach_hang.setText("");
                No_cu.setText("0.0");
            }
         }
         else
         {
             thongbao_text("Không có mặt hàng để thanh toán", Color.BLUE);
         }
         barCode_grabFocus();
     }
     public void in_ma_vach()
     {
         int return_val = get_info_barcode();
        if(return_val == 0)
        {
            return_val = show_message("Xác nhận in barcode cho sản phẩm\n");
            if(return_val == 0)
            {
                Print_barcode();
            }
        }
        ComboBox_history.setEnabled(true);
     }
     public void cap_nhap_gia_ten()
     {
         Updated_mode = true;
        botton_update_database.setEnabled(true);

        clear_Updated_list();
        clear_table(dm_info_SP);
        clear_table(dm_hoa_don);
        clear_Paid_list();
        barCode_grabFocus();
     }
     public double calprice(String foo)
     {
        double result = -99999.0;
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        if(!foo.trim().isEmpty())
        {        
            try {
                
                result = Double.parseDouble(engine.eval(foo).toString());
            } catch (ScriptException ex) {
                result = -99999;
            }
        }
        return result;
    }
     public String formatNumber(Double inNum, int mode)
     {
         String return_string;
         if(mode == 0){
             return_string = slFormat0.format(inNum);
         }else if(mode == 1){
             return_string = slFormat1.format(inNum);
         }else{
             return_string = slFormat2.format(inNum);
         }
         return return_string;
     }
     public void consilidated_table()
     {
         int row_count = bang_tim_info_SP.getRowCount();
        Object temp;
        double result = 1.0;
        for(int i =0; i < row_count; i++)
        {
            for(int j = 2; j < 4; j++)
            {
                temp = bang_tim_info_SP.getValueAt(i, j);
                //calprice(temp.toString(), result);
                result = calprice(temp.toString());
                if(result != -99999.0){
                    bang_tim_info_SP.setValueAt(result, i, j);
                }else{
                    bang_tim_info_SP.setValueAt(temp, i, j);
                }
                System.out.println("result: " + result + " temp " + temp.toString() + "row " + i);
            }
        }
     }
     public void xem_lai_hoa_don()
     {
         history_tracking.setVisible(true);
     }
     public void refresh()
     {
         clear_Paid_list();
        clear_Updated_list();
        clear_hand_list();
        clear_table(dm_hoa_don);
        clear_table(dm_info_SP);
        normal_mode(false);
        Updated_mode = false;
        field_barcode_maSP.setText("");
        field_tim_MaSP.setText("");
        field_tim_tenSP.setText("");

        barCode_grabFocus();
     }
     private void setup_configuration() {
         Configuration_system SVtool = new Configuration_system();
         boolean result = SVtool.LoadConfigureInfo();
         if(!result){
             SVtool.defaulConfigureInfo();
         }
         SVtool.setVisible(true);
     }
     private boolean savebill(String saveFile){
         boolean return_val = true;
         String unknownComsumer = "......................";
         if(No_cu.getText().isEmpty()){
             No_cu.setText("0.0");
         }
         try{
             Double.parseDouble(No_cu.getText());
         }catch(NumberFormatException e){
             show_message("Lỗi, Coi lại số nợ");
             return_val = false;
         }
         if(return_val){
            try{
               FileOutputStream fos = new FileOutputStream(saveFile);
               Writer out = new OutputStreamWriter(fos, "UTF8");
               {
                   int paid_count = Paid_MaSP.size();
                   if(ten_khach_hang.getText().trim().isEmpty()){
                       ten_khach_hang.setText(unknownComsumer);
                   }
                   out.write("==+"+ ten_khach_hang.getText() + "\n");
                   for(int i = 0; i < paid_count; i++)
                   {
                       out.write("===" + Paid_MaSP.get(i) + "<>"
                               + Paid_TenSP.get(i) + "<>"
                               + Paid_GiaSSP.get(i) + "<>"
                               + Paid_GiaLSP.get(i) + "<>"
                               + Paid_SoLuongSP.get(i) + "<>"
                               + Paid_TongGiaSP.get(i) + "\n");
                   }
                   out.write("==-"+ No_cu.getText() + "\n");
                   out.close();
               }
            }catch (Exception e){//Catch exception if any
                thongbao_text("[Lỗi] Không mở được file" + saveFile, Color.RED);
                return_val = false;
            }
         }
         return return_val;
     }
     private void buttonSaveBillsetText(int i){
         Button_luahoadon.setText("Lưu hóa đơn [" + i +"]");
         if(i != 0){
             Button_luahoadon.setForeground(Color.red);
         }else{
             Button_luahoadon.setForeground(Color.blue);
         }
     }
     private void buttonSaveBill(){
        boolean result ;
        if(!Paid_MaSP.isEmpty() && Money_count_Items != 0){
            int curSaveBillIdx = saveBillCount%saveBillBound;
            String savefile = saveBillFile + curSaveBillIdx;
            result = savebill(savefile);
            if(result){
                buttonSaveBillsetText(saveBillIdxNoti++);
                saveBillCount ++;
                ComboBox_SaveBill.insertItemAt("Hóa đơn[" + saveBillCount + "]" 
                        +ten_khach_hang.getText().trim(), curSaveBillIdx);
                    if(ComboBox_SaveBill.getItemCount() > saveBillBound){
                    ComboBox_SaveBill.removeItemAt(curSaveBillIdx + 1);
                }
                clear_table(dm_hoa_don);
                clear_Paid_list();
            }else{
                thongbao_text("[Lỗi] Lưa không thành công", Color.RED);
            }
        }else{
            thongbao_text("Không có sản phẩm", Color.blue);
        }
      
     }
     private void LoadSavedBill(){
         if(ComboBox_SaveBill.getItemCount() > 0){
            int idxFile = ComboBox_SaveBill.getSelectedIndex();
            String loadFile = saveBillFile + idxFile;
            int count = 1;
            clear_Paid_list();
            clear_table(dm_hoa_don);
            try{
                FileInputStream in = new FileInputStream(loadFile);
                thongbao_text(loadFile, Color.blue);
                try (BufferedReader bufffile = new BufferedReader(new InputStreamReader(in, "UTF8"))){
                    String strLine;
                    strLine = bufffile.readLine();
                    while (strLine != null) {
                        if(strLine.substring(0, 3).equals("==+")){
                            ten_khach_hang.setText(strLine.substring(3));
                        }else if(strLine.substring(0, 3).equals("===")){
                            String sdata[] = strLine.substring(3).split("<>");
                            Vector  sVector = new Vector();
                            sVector.add(count++);
                            sVector.add(sdata[0]);  //ma sp
                            sVector.add(sdata[1]);  //ten sp
                            sVector.add(sdata[2]);  //gia s
                            sVector.add(sdata[3]);  //gia l
                            sVector.add(sdata[4]);  //so luong
                            sVector.add(sdata[5]);  // tien
                            sVector.add(Boolean.TRUE);
                            dm_hoa_don.addRow(sVector);
                            dm_hoa_don.fireTableDataChanged();
                            Paid_MaSP.add(sdata[0]);
                            Paid_TenSP.add(sdata[1]);
                            Paid_GiaSSP.add(Double.parseDouble(sdata[2]));
                            Paid_GiaLSP.add(Double.parseDouble(sdata[3]));
                            Paid_SoLuongSP.add(Double.parseDouble(sdata[4]));
                            Paid_TongGiaSP.add(Double.parseDouble(sdata[5]));
                            //

                        }else if(strLine.substring(0, 3).equals("==-")){
                            No_cu.setText(strLine.substring(3));
                        }
                        strLine = bufffile.readLine();
                    }
                    if(dm_hoa_don.getRowCount() > 0){
                        add_Paid_row();
                    }else{
                        clear_Paid_list();
                        clear_table(dm_hoa_don);
                        thongbao_text("[Lỗi] refresh để làm lại", Color.RED);
                    }
                }
                in.close();
            }catch (Exception e){//Catch exception if any
                thongbao_text("Lỗi" + e.getMessage(), Color.RED);
            }
        }else{
            thongbao_text("Chưa có hóa đơn nào được lựu", Color.blue);
        }
     }
     private void add_to_history(){
         String sHistory = Cbm_history.getSelectedItem().toString();
         System.out.println("sHistory:" + sHistory + ", file name " + file_path);
         if(!sHistory.isEmpty())
         {
            String name[]   = sHistory.split("<>");
            boolean        found = false;
            int count = 1;
            ten_khach_hang.setText(name[1]);

            try{
               FileInputStream in = new FileInputStream(file_path);
               try (BufferedReader bufffile = new BufferedReader(new InputStreamReader(in, "UTF8"))) 
               {
                   String strLine;
                   strLine = bufffile.readLine();
                   OUTER:
                   while (strLine != null) {                       
                       if(strLine.contains(sHistory))
                       {
                           found = true;
                           clear_Paid_list();
                           clear_table(dm_hoa_don);
                       }
                       if (found) {
                           switch (strLine.substring(0, 3)) {
                               case "---":
                                   {
                                       String sdata[] = strLine.substring(3).split("<>");
                                       Vector  sVector = new Vector();
                                       sVector.add(count++);
                                       sVector.add(sdata[0]);  //ma sp
                                       sVector.add(sdata[1]);  //ten sp
                                       sVector.add(sdata[4]);  //gia s
                                       sVector.add(sdata[5]);  //gia l
                                       sVector.add(sdata[2]);  //so luong
                                       sVector.add(sdata[3]);  // tien
                                       sVector.add(Boolean.TRUE);
                                       dm_hoa_don.addRow(sVector);
                                       dm_hoa_don.fireTableDataChanged();
                                       Paid_MaSP.add(sdata[0]);
                                       Paid_TenSP.add(sdata[1]);
                                       Paid_GiaSSP.add(Double.parseDouble(sdata[4]));
                                       Paid_GiaLSP.add(Double.parseDouble(sdata[5]));
                                       Paid_SoLuongSP.add(Double.parseDouble(sdata[2]));
                                       Paid_TongGiaSP.add(Double.parseDouble(sdata[3]));
                                       display_text_paylist();
                                       break;
                                   }
                               case "==>":
                                   {
                                       Vector  sVector = new Vector();
                                       sVector.add(null);
                                       sVector.add(null);
                                       sVector.add(null);
                                       sVector.add(null);
                                       sVector.add(null);
                                       sVector.add("1");  //so luong
                                       sVector.add("1.11");  // tien
                                       sVector.add(Boolean.FALSE);
                                       dm_hoa_don.addRow(sVector);
                                       cal_price_items();
                                       calculate_total_price();
                                       break;
                                   }
                               case "==-": //no cu
                               {
                                   No_cu.setText(strLine.substring(3));
                                   break OUTER;
                               }
                           }
                       }
                       strLine = bufffile.readLine();
                   }
               }
               in.close();
           }catch (Exception e){//Catch exception if any
               System.err.println("Error: " + e.getMessage());
           }
         }
     }
     public void exit_program()
     {
         return_result = show_message("Thoát chương trình?");
         if(return_result == 0)
         {
            SauVan.reorder_file_data();
            System.exit(0);
         }
         unlockFile();
     }
     public void thongbao_text(String message, Color color)
     {
         Thong_bao_text.setText(message);
         Thong_bao_text.setForeground(color);
     }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel_thongTin = new javax.swing.JPanel();
        field_tim_tenSP = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        botton_add_prod2Paylist = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        bang_tim_info_SP = new javax.swing.JTable();
        botton_update_database = new javax.swing.JButton();
        botton_clear_info_table = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        field_tim_MaSP = new javax.swing.JTextField();
        field_barcode_maSP = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel_hoa_don = new javax.swing.JPanel();
        jScrollPane_banghoadon = new javax.swing.JScrollPane();
        bang_HoaDon = new javax.swing.JTable();
        Thong_bao_text = new javax.swing.JLabel();
        date_time = new javax.swing.JLabel();
        select_price = new javax.swing.JCheckBox();
        date_time1 = new javax.swing.JLabel();
        ten_khach_hang = new javax.swing.JTextField();
        Button_them = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        No_cu = new javax.swing.JTextField();
        Button_luahoadon = new javax.swing.JButton();
        ComboBox_SaveBill = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        botton_history_tracking = new javax.swing.JButton();
        botton_update_mode = new javax.swing.JButton();
        botton_thanhtoan_mode = new javax.swing.JButton();
        botton_pay_print_bill = new javax.swing.JButton();
        field_price_other = new javax.swing.JTextField();
        field_return_money = new javax.swing.JTextField();
        Label_other_price1 = new javax.swing.JLabel();
        Label_other_price = new javax.swing.JLabel();
        ComboBox_history = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        botton_print_barcode = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        menu_file = new javax.swing.JMenu();
        menu_thoat = new javax.swing.JMenuItem();
        Menu_tool = new javax.swing.JMenu();
        menu_inhoadon = new javax.swing.JMenuItem();
        menu_inmavach = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        menu_danhbadt = new javax.swing.JMenuItem();
        menu_xemlaihoadon = new javax.swing.JMenuItem();
        menu_capnhapgiaten = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        menu_lamtuoi = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Cửa hàng tạp hóa SÁU VÂN");
        setIconImage(Toolkit.getDefaultToolkit().getImage("/sale/img/bar-code.png"));
        setIconImages(null);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                formComponentMoved(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        jPanel_thongTin.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jPanel_thongTin.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        field_tim_tenSP.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        field_tim_tenSP.setForeground(new java.awt.Color(0, 0, 102));
        field_tim_tenSP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                field_tim_tenSPKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 102));
        jLabel2.setText("Tìm SP (tên) :");

        botton_add_prod2Paylist.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        botton_add_prod2Paylist.setForeground(new java.awt.Color(102, 0, 0));
        botton_add_prod2Paylist.setText("Thêm sp vào hóa đơn");
        botton_add_prod2Paylist.setToolTipText("");
        botton_add_prod2Paylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botton_add_prod2PaylistActionPerformed(evt);
            }
        });

        bang_tim_info_SP.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        bang_tim_info_SP.setForeground(new java.awt.Color(0, 0, 204));
        bang_tim_info_SP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã sản phẩm", "Tên sản phẩm", "Giá S sp", "Giá L sp", "Số lượng", "chọn/bỏ chọn"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        bang_tim_info_SP.setGridColor(new java.awt.Color(0, 0, 204));
        bang_tim_info_SP.setRowHeight(26);
        jScrollPane1.setViewportView(bang_tim_info_SP);

        botton_update_database.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        botton_update_database.setForeground(new java.awt.Color(102, 0, 0));
        botton_update_database.setText("Cập nhập");
        botton_update_database.setToolTipText("cap nhap ");
        botton_update_database.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botton_update_databaseActionPerformed(evt);
            }
        });

        botton_clear_info_table.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        botton_clear_info_table.setForeground(new java.awt.Color(102, 0, 0));
        botton_clear_info_table.setText("Hủy cập nhập");
        botton_clear_info_table.setToolTipText("huy cap nhap");
        botton_clear_info_table.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botton_clear_info_tableActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 102));
        jLabel3.setText("Tìm SP(Mã) :");

        field_tim_MaSP.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        field_tim_MaSP.setForeground(new java.awt.Color(0, 0, 102));
        field_tim_MaSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                field_tim_MaSPActionPerformed(evt);
            }
        });
        field_tim_MaSP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                field_tim_MaSPKeyReleased(evt);
            }
        });

        field_barcode_maSP.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        field_barcode_maSP.setForeground(new java.awt.Color(0, 0, 102));
        field_barcode_maSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                field_barcode_maSPActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 102));
        jLabel1.setText("Barcode SP");

        jPanel_hoa_don.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin hóa đơn", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Times New Roman", 1, 14), new java.awt.Color(102, 0, 0))); // NOI18N
        jPanel_hoa_don.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N

        bang_HoaDon.setAutoCreateRowSorter(true);
        bang_HoaDon.setBorder(new javax.swing.border.MatteBorder(null));
        bang_HoaDon.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        bang_HoaDon.setForeground(java.awt.Color.red);
        bang_HoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stt", "Mã SP", "Tên SP", "Giá S SP", "Giá L SP", "Số Lượng SP", "Thành Tiền", "Chọn mua"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        bang_HoaDon.setToolTipText("");
        bang_HoaDon.setGridColor(new java.awt.Color(0, 0, 153));
        bang_HoaDon.setIntercellSpacing(new java.awt.Dimension(0, 0));
        bang_HoaDon.setRowHeight(30);
        bang_HoaDon.setRowSorter(null);
        bang_HoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bang_HoaDonMouseClicked(evt);
            }
        });
        bang_HoaDon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                bang_HoaDonKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                bang_HoaDonKeyTyped(evt);
            }
        });
        jScrollPane_banghoadon.setViewportView(bang_HoaDon);

        Thong_bao_text.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        Thong_bao_text.setForeground(new java.awt.Color(0, 0, 204));
        Thong_bao_text.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Thong_bao_text.setText("info by text");
        Thong_bao_text.setFocusable(false);

        date_time.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        date_time.setForeground(new java.awt.Color(0, 0, 153));
        date_time.setText("Date- Time");

        select_price.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        select_price.setText("Giá Sỹ/ giá lẻ");
        select_price.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                select_priceActionPerformed(evt);
            }
        });

        date_time1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        date_time1.setForeground(new java.awt.Color(0, 0, 153));
        date_time1.setText("Tên khách hàng:");

        ten_khach_hang.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        ten_khach_hang.setText(".....................");
        ten_khach_hang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ten_khach_hangMouseClicked(evt);
            }
        });
        ten_khach_hang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ten_khach_hangActionPerformed(evt);
            }
        });

        Button_them.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        Button_them.setForeground(new java.awt.Color(0, 0, 204));
        Button_them.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sale/img/button_add.png"))); // NOI18N
        Button_them.setText("Thêm");
        Button_them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_themActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel4.setText("Nợ cũ:");

        No_cu.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        No_cu.setText("0.0");
        No_cu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                No_cuMouseClicked(evt);
            }
        });
        No_cu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                No_cuKeyReleased(evt);
            }
        });

        Button_luahoadon.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        Button_luahoadon.setForeground(new java.awt.Color(0, 0, 204));
        Button_luahoadon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sale/img/button_SavebillInfo.png"))); // NOI18N
        Button_luahoadon.setText("Lưu hóa đơn [0]");
        Button_luahoadon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_luahoadonActionPerformed(evt);
            }
        });

        ComboBox_SaveBill.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        ComboBox_SaveBill.setForeground(new java.awt.Color(0, 0, 153));
        ComboBox_SaveBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBox_SaveBillActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_hoa_donLayout = new javax.swing.GroupLayout(jPanel_hoa_don);
        jPanel_hoa_don.setLayout(jPanel_hoa_donLayout);
        jPanel_hoa_donLayout.setHorizontalGroup(
            jPanel_hoa_donLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_hoa_donLayout.createSequentialGroup()
                .addComponent(date_time, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(date_time1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ten_khach_hang, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(No_cu, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91)
                .addComponent(Button_them)
                .addGap(29, 29, 29)
                .addComponent(Button_luahoadon)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ComboBox_SaveBill, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(select_price))
            .addComponent(Thong_bao_text, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane_banghoadon)
        );
        jPanel_hoa_donLayout.setVerticalGroup(
            jPanel_hoa_donLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_hoa_donLayout.createSequentialGroup()
                .addGroup(jPanel_hoa_donLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(select_price)
                    .addComponent(date_time)
                    .addComponent(date_time1)
                    .addComponent(ten_khach_hang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_them)
                    .addComponent(jLabel4)
                    .addComponent(No_cu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_luahoadon)
                    .addComponent(ComboBox_SaveBill, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane_banghoadon, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Thong_bao_text, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel_hoa_donLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {Button_them, No_cu, date_time, date_time1, jLabel4, ten_khach_hang});

        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(102, 0, 0));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sale/img/button_exit.png"))); // NOI18N
        jButton1.setToolTipText("Thoát");
        jButton1.setIconTextGap(1);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        botton_history_tracking.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        botton_history_tracking.setForeground(new java.awt.Color(102, 0, 0));
        botton_history_tracking.setText("Xem lại h.đơn");
        botton_history_tracking.setToolTipText("xem lại các hóa đơn đã bán, mở file sau đó chọn năm/ chọn tháng/ rồi chọn ngày");
        botton_history_tracking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botton_history_trackingActionPerformed(evt);
            }
        });

        botton_update_mode.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        botton_update_mode.setForeground(new java.awt.Color(102, 0, 0));
        botton_update_mode.setText("Cập nhập giá,tên");
        botton_update_mode.setToolTipText("Cập nhập giá cả, tên sản phẩm. số lượng hàng nhập ....");
        botton_update_mode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botton_update_modeActionPerformed(evt);
            }
        });

        botton_thanhtoan_mode.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        botton_thanhtoan_mode.setForeground(new java.awt.Color(102, 0, 0));
        botton_thanhtoan_mode.setText("Thanh toán");
        botton_thanhtoan_mode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botton_thanhtoan_modeActionPerformed(evt);
            }
        });

        botton_pay_print_bill.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        botton_pay_print_bill.setForeground(new java.awt.Color(102, 0, 0));
        botton_pay_print_bill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sale/img/menu_Printer.png"))); // NOI18N
        botton_pay_print_bill.setText("In hóa đơn");
        botton_pay_print_bill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botton_pay_print_billActionPerformed(evt);
            }
        });

        field_price_other.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        field_price_other.setText("0");
        field_price_other.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                field_price_otherActionPerformed(evt);
            }
        });
        field_price_other.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                field_price_otherKeyReleased(evt);
            }
        });

        field_return_money.setEditable(false);
        field_return_money.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        field_return_money.setText("0");
        field_return_money.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                field_return_moneyActionPerformed(evt);
            }
        });

        Label_other_price1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        Label_other_price1.setText("Tiền thối lại");

        Label_other_price.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        Label_other_price.setText("Tiền lấy vào :");

        ComboBox_history.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        ComboBox_history.setForeground(new java.awt.Color(102, 0, 0));
        ComboBox_history.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBox_historyActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 153));
        jLabel5.setText("Hóa đơn đã bán:");

        botton_print_barcode.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        botton_print_barcode.setForeground(new java.awt.Color(102, 0, 0));
        botton_print_barcode.setText("In mã");
        botton_print_barcode.setToolTipText("In mã vạch");
        botton_print_barcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botton_print_barcodeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                    .addComponent(ComboBox_history, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Label_other_price)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(field_price_other, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Label_other_price1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(field_return_money, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 245, Short.MAX_VALUE)
                .addComponent(botton_pay_print_bill)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botton_thanhtoan_mode)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botton_update_mode)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botton_history_tracking)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botton_print_barcode)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {Label_other_price, Label_other_price1});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(botton_history_tracking, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(botton_update_mode, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(botton_thanhtoan_mode, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(botton_pay_print_bill, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(botton_print_barcode, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Label_other_price)
                            .addComponent(field_price_other, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Label_other_price1)
                            .addComponent(field_return_money, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(8, 8, 8)
                        .addComponent(ComboBox_history)))
                .addGap(11, 11, 11))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {botton_history_tracking, botton_pay_print_bill, botton_thanhtoan_mode, botton_update_mode, jButton1});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {Label_other_price, Label_other_price1, field_price_other, field_return_money});

        jButton2.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jButton2.setForeground(new java.awt.Color(102, 0, 0));
        jButton2.setText("refresh");
        jButton2.setToolTipText("refresh");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_thongTinLayout = new javax.swing.GroupLayout(jPanel_thongTin);
        jPanel_thongTin.setLayout(jPanel_thongTinLayout);
        jPanel_thongTinLayout.setHorizontalGroup(
            jPanel_thongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_thongTinLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(field_barcode_maSP, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(field_tim_MaSP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(field_tim_tenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botton_add_prod2Paylist)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botton_update_database)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botton_clear_info_table)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2))
            .addComponent(jPanel_hoa_don, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        jPanel_thongTinLayout.setVerticalGroup(
            jPanel_thongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_thongTinLayout.createSequentialGroup()
                .addGroup(jPanel_thongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(field_tim_MaSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(field_tim_tenSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botton_add_prod2Paylist)
                    .addComponent(botton_update_database)
                    .addComponent(botton_clear_info_table)
                    .addComponent(jLabel1)
                    .addComponent(field_barcode_maSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel_hoa_don, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel_thongTinLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {botton_add_prod2Paylist, botton_clear_info_table, botton_update_database, field_barcode_maSP, field_tim_MaSP, field_tim_tenSP, jButton2, jLabel1, jLabel2, jLabel3});

        menu_file.setText("File");

        menu_thoat.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        menu_thoat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sale/img/menu_exit.png"))); // NOI18N
        menu_thoat.setText("Thoát");
        menu_thoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_thoatActionPerformed(evt);
            }
        });
        menu_file.add(menu_thoat);

        jMenuBar1.add(menu_file);

        Menu_tool.setText("Công cụ");

        menu_inhoadon.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        menu_inhoadon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sale/img/menu_Printer.png"))); // NOI18N
        menu_inhoadon.setText("In hóa đơn");
        menu_inhoadon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_inhoadonActionPerformed(evt);
            }
        });
        Menu_tool.add(menu_inhoadon);

        menu_inmavach.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
        menu_inmavach.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sale/img/menu_barcode.png"))); // NOI18N
        menu_inmavach.setText("In mã vạch");
        menu_inmavach.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_inmavachActionPerformed(evt);
            }
        });
        Menu_tool.add(menu_inmavach);
        Menu_tool.add(jSeparator2);

        menu_danhbadt.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        menu_danhbadt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sale/img/menu_address.png"))); // NOI18N
        menu_danhbadt.setText("Danh bạ điện thoại");
        menu_danhbadt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_danhbadtActionPerformed(evt);
            }
        });
        Menu_tool.add(menu_danhbadt);

        menu_xemlaihoadon.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        menu_xemlaihoadon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sale/img/menu_history.png"))); // NOI18N
        menu_xemlaihoadon.setText("Xem lại hóa đơn");
        menu_xemlaihoadon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_xemlaihoadonActionPerformed(evt);
            }
        });
        Menu_tool.add(menu_xemlaihoadon);

        menu_capnhapgiaten.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_MASK));
        menu_capnhapgiaten.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sale/img/menu_update.png"))); // NOI18N
        menu_capnhapgiaten.setText("Cập nhập giá/tên");
        menu_capnhapgiaten.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_capnhapgiatenActionPerformed(evt);
            }
        });
        Menu_tool.add(menu_capnhapgiaten);
        Menu_tool.add(jSeparator1);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sale/img/menu_tool.png"))); // NOI18N
        jMenuItem1.setText("cấu hình hệ thống");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        Menu_tool.add(jMenuItem1);

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sale/img/menu_load_config.png"))); // NOI18N
        jMenuItem2.setText("load cấu hình");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        Menu_tool.add(jMenuItem2);
        Menu_tool.add(jSeparator3);

        menu_lamtuoi.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        menu_lamtuoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sale/img/menu_refresh.png"))); // NOI18N
        menu_lamtuoi.setText("Làm tươi hệ thống");
        menu_lamtuoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_lamtuoiActionPerformed(evt);
            }
        });
        Menu_tool.add(menu_lamtuoi);

        jMenuBar1.add(Menu_tool);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_thongTin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel_thongTin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_formKeyReleased

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseReleased

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_formKeyPressed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        // TODO add your handling code here:
        
        
    }//GEN-LAST:event_formComponentShown
    public void display_result2infoTable(Vector array_result)
    {
        int index_found;
        Vector update_info_sp = new Vector();
        search_list.removeAllElements();
        //clear_table(dm_info_SP);
        int old_result_count = dm_info_SP.getRowCount();
        
        for(int i = old_result_count - 1; i >= 0; i --)
        {
            if(!(boolean)dm_info_SP.getValueAt(i, 5))
            {
                dm_info_SP.removeRow(i);
            }
            else
            {
                search_list.add(dm_info_SP.getValueAt(i, 0).toString());
            }
        }
        for(int i = 0; i< array_result.size(); i++)
        {
            index_found = Integer.parseInt(array_result.get(i).toString());
            int new_masp_size = search_list.size();
            int ibreak = 0;
            update_info_sp = SauVan.get_sp(index_found);
            System.out.println("siiiize " + new_masp_size);
            for(int j = 0 ; j < new_masp_size; j++)
            {
                if(search_list.get(j).equals(update_info_sp.get(0)))
                {
                    ibreak = -1;
                    break;
                }
            }
            if(ibreak != -1)
            {
                update_info_sp.add(Boolean.FALSE);
                dm_info_SP.insertRow(0,update_info_sp);
            }
            
        }
    }
    public int diff_slFormat()
    {
        int size = Paid_SoLuongSP.size();
        double dnum = 0.0;
        double dnum1 = 0.0;
        int    return_val = 0;
        int    inum;
        int    diff;
        for(int i = 0; i < size ; i++)
        {
            dnum    = Paid_SoLuongSP.get(i);
            inum    = Paid_SoLuongSP.get(i).intValue();
            dnum1   = Double.parseDouble(slFormat1.format(Paid_SoLuongSP.get(i)));
            if(dnum == inum ){
                diff = 0;
            }else if(dnum == dnum1){
                diff = 1;
            }else{
                diff = 2;
            }
            if(diff > return_val){
                return_val = diff;
                if(return_val == 2){
                    break;
                }
            }
        }
        return return_val;
    }
    public void clear_Updated_list()
    {
        New_MaSP.clear();
    }
    public void clear_hand_list()
    {
        hand_offset = 1;
    }
    public void clear_Paid_list()
    {
        Paid_GiaLSP.clear();
        Paid_GiaSSP.clear();
        Paid_MaSP.clear();
        Paid_TenSP.clear();
        Paid_TongGiaSP.clear();
        Paid_SoLuongSP.clear();  
    }
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        barCode_grabFocus();
    }//GEN-LAST:event_formMouseClicked

    private void formComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentMoved
        // TODO add your handling code here:
        barCode_grabFocus();
    }//GEN-LAST:event_formComponentMoved

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        // TODO add your handling code here:
        barCode_grabFocus();
    }//GEN-LAST:event_formComponentResized

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        // TODO add your handling code here:
        barCode_grabFocus();
    }//GEN-LAST:event_formWindowGainedFocus

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        refresh();
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void botton_print_barcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botton_print_barcodeActionPerformed
        // TODO add your handling code here:
        in_ma_vach();
        
    }//GEN-LAST:event_botton_print_barcodeActionPerformed

    private void ComboBox_historyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBox_historyActionPerformed
        // TODO add your handling code here:
        add_to_history();
        barCode_grabFocus();
    }//GEN-LAST:event_ComboBox_historyActionPerformed

    private void field_return_moneyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_field_return_moneyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_field_return_moneyActionPerformed

    private void field_price_otherKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_field_price_otherKeyReleased
        // TODO add your handling code here:
        get_moneyformuser();
    }//GEN-LAST:event_field_price_otherKeyReleased

    private void field_price_otherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_field_price_otherActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_field_price_otherActionPerformed

    private void botton_pay_print_billActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botton_pay_print_billActionPerformed
        // TODO add your handling code here:
        in_hoa_don();
    }//GEN-LAST:event_botton_pay_print_billActionPerformed

    private void botton_thanhtoan_modeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botton_thanhtoan_modeActionPerformed
        // TODO add your handling code here:
        diff_slFormat();
        pre_paid();
        
    }//GEN-LAST:event_botton_thanhtoan_modeActionPerformed

    private void botton_update_modeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botton_update_modeActionPerformed
        // TODO add your handling code here:
        cap_nhap_gia_ten();
    }//GEN-LAST:event_botton_update_modeActionPerformed

    private void botton_history_trackingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botton_history_trackingActionPerformed
        // TODO add your handling code here:
        history_tracking.setVisible(true);
    }//GEN-LAST:event_botton_history_trackingActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        exit_program();
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void No_cuKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_No_cuKeyReleased
        // TODO add your handling code here:
        thongbao_text("", Color.black );
        if(!No_cu.getText().isEmpty())
        {
            try {
                dno_cu = Double.parseDouble(No_cu.getText());
            }
            catch (NumberFormatException a)
            {
                thongbao_text("Nhập sai!", Color.RED);
            }
        }
    }//GEN-LAST:event_No_cuKeyReleased

    private void No_cuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_No_cuMouseClicked
        // TODO add your handling code here:
        No_cu.setText("");
    }//GEN-LAST:event_No_cuMouseClicked

    private void Button_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_themActionPerformed
        // TODO add your handling code here:
        if(hand_offset <=40)
        {
            String themtay_hoadon = "SV_THEMTAY" + hand_offset;
            int them_tay_index  = SauVan.tim_sp(themtay_hoadon);
            addtopay(them_tay_index);
            hand_offset ++;
        }
        else
        {
            thongbao_text("chỉ được thêm tối đa 40 sp! bấm refresh để làm lại", Color.BLUE);
        }
        barCode_grabFocus();
    }//GEN-LAST:event_Button_themActionPerformed

    private void ten_khach_hangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ten_khach_hangActionPerformed
        // TODO add your handling code here:
        barCode_grabFocus();        
    }//GEN-LAST:event_ten_khach_hangActionPerformed

    private void ten_khach_hangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ten_khach_hangMouseClicked
        // TODO add your handling code here:
        ten_khach_hang.setText("");
    }//GEN-LAST:event_ten_khach_hangMouseClicked

    private void select_priceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_select_priceActionPerformed
        // TODO add your handling code here:
        //cap nhap cach tinh gia
        cal_price_items();
        field_barcode_maSP.grabFocus();
    }//GEN-LAST:event_select_priceActionPerformed

    private void bang_HoaDonKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bang_HoaDonKeyTyped
        // TODO add your handling code here:
        if(hd_inMethodClean)
        {
            hd_inMethodClean = false;
            int Scol    = bang_HoaDon.getSelectedColumn();
            int Srow    = bang_HoaDon.getSelectedRow();
            //ddddd
            System.out.println("s col,row = " + Scol + ", " + Srow + " table row"
                + bang_HoaDon.getRowCount());
            if(2 == Scol && Scol <= 5 && (Srow +1 )!= bang_HoaDon.getRowCount())
            {
            }
        }
    }//GEN-LAST:event_bang_HoaDonKeyTyped

    private void bang_HoaDonKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bang_HoaDonKeyReleased
        // TODO add your handling code here:
        int row_count = bang_HoaDon.getRowCount();
        Object temp;
        //int i = bang_HoaDon.getSelectedRow();
        //if( 0 <= i && i <= (bang_HoaDon.getRowCount() -2))
        for(int i =0; i < row_count-1; i++)
        {
            temp = bang_HoaDon.getValueAt(i, 2);
            Paid_TenSP.set(i, temp.toString());
            temp = bang_HoaDon.getValueAt(i, 3);
            Paid_GiaSSP.set(i, Double.parseDouble(temp.toString()));
            temp = bang_HoaDon.getValueAt(i, 4);
            Paid_GiaLSP.set(i, Double.parseDouble(temp.toString()));
            temp = bang_HoaDon.getValueAt(i, 5);
            Paid_SoLuongSP.set(i,Double.parseDouble(temp.toString()));

            calculate_item_paid_row(i);
            update_item_row(i);
            System.out.println("@@" + Paid_GiaSSP.get(i) + " # " + Paid_GiaLSP.get(i) +
                " # " + Paid_SoLuongSP.get(i) + "#" +Paid_TongGiaSP.get(i));
        }
        update_total_Paid_row();
    }//GEN-LAST:event_bang_HoaDonKeyReleased

    private void bang_HoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bang_HoaDonMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount() == 1)    //a click
        {
            hd_inMethodClean = true;
        }
        //        if(!Process_pay_mode)
        {
            update_total_Paid_row();
        }
    }//GEN-LAST:event_bang_HoaDonMouseClicked

    private void field_barcode_maSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_field_barcode_maSPActionPerformed
        // TODO add your handling code here:
        if(!field_barcode_maSP.getText().isEmpty())
        {
            curMaSP        = field_barcode_maSP.getText().toUpperCase().trim();
            field_barcode_maSP.setText("");
            //tim_tenSP.setText(curMaSP);
            return_result   = SauVan.tim_sp(curMaSP);
            thongbao_text("Mã Sản phẩm :" + curMaSP, Color.RED);
            if(return_result == -1) //not found in data
            {
                botton_update_database.setEnabled(true);
                add_info_SP2BTK(curMaSP);
            }
            else                    //found indata
            {
                if(Updated_mode)    //update mode
                {
                    //System.out.println("mode updates san pham " + curMaSP);
                    //clear_list_sp();
                    clear_table(dm_hoa_don);
                    update_info_SP2BTK(curMaSP);
                }
                else                //add into pay list
                {
                    System.out.println("mode ban san pham " + curMaSP + " index "+ return_result);
                    addtopay(return_result);
                }
            }
        }
    }//GEN-LAST:event_field_barcode_maSPActionPerformed

    private void field_tim_MaSPKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_field_tim_MaSPKeyReleased
        // TODO add your handling code here:
        String tim_bar_codeSP = field_tim_MaSP.getText().trim();
        if(!tim_bar_codeSP.isEmpty())
        {
            Vector array_result = SauVan.timThongMinh_MaSP(field_tim_MaSP.getText().trim());

            //clear table
            display_result2infoTable(array_result);

            if(array_result.size() > 0)
            {
                botton_add_prod2Paylist.setEnabled(true);
                botton_update_database.setEnabled(true);
            }
            else
            {
                botton_add_prod2Paylist.setEnabled(false);
                botton_update_database.setEnabled(false);
            }
        }
        else
        {
            //clear_table(dm_info_SP);
            int old_result_count = dm_info_SP.getRowCount();

            for(int i = old_result_count - 1; i >= 0; i --)
            {
                if(!(boolean)dm_info_SP.getValueAt(i, 5))
                {

                    dm_info_SP.removeRow(i);
                }
            }
            botton_add_prod2Paylist.setEnabled(false);
            botton_update_database.setEnabled(false);
        }
    }//GEN-LAST:event_field_tim_MaSPKeyReleased

    private void field_tim_MaSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_field_tim_MaSPActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_field_tim_MaSPActionPerformed

    private void botton_clear_info_tableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botton_clear_info_tableActionPerformed
        // TODO add your handling code here:
        clear_table(dm_info_SP);
        clear_Updated_list();
        Updated_mode    = false;
        botton_update_database.setEnabled(false);
        jScrollPane_banghoadon.setVisible(true);
        barCode_grabFocus();
    }//GEN-LAST:event_botton_clear_info_tableActionPerformed

    private void botton_update_databaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botton_update_databaseActionPerformed
        // TODO add your handling code here:
        //get and check all data form table
        consilidated_table();
        int row_Count;
        row_Count = bang_tim_info_SP.getRowCount();
        double  test_gia;
        int     test_soluong;
        Object  sample;
        thongbao_text("", Color.BLUE);
        for(int i = 0; i < row_Count; i++)
        {
            try{
                sample = bang_tim_info_SP.getValueAt(i, 2);
                test_gia    = Double.parseDouble(sample.toString());
                sample = bang_tim_info_SP.getValueAt(i, 3);
                test_gia    = Double.parseDouble(sample.toString());
                sample = bang_tim_info_SP.getValueAt(i, 4);
                test_soluong    = Integer.parseInt(sample.toString());
                if(test_soluong <= 0)
                {
                    thongbao_text("[lỗi nhập số]bảng thông tin sản phẩm, cột 5 hàng " + (i+1), Color.red);
                    row_Count = 0;  // break next loop
                    break;
                }
            } //Catch error if input was not a number and repeat while loop.
            catch ( NumberFormatException e){
                thongbao_text("[lỗi nhập số] Hàng " + (i+1),Color.RED);
                row_Count = 0;  // break next loop
                break;
            }
        }

        for(int i = 0; i < row_Count; i++)
        {
            thongbao_text("Đang cập nhập sản phẩm :" + bang_tim_info_SP.getValueAt(i, 2),Color.RED);
            Vector inputSP = new Vector();
            inputSP.add(bang_tim_info_SP.getValueAt(i, 0));
            inputSP.add(bang_tim_info_SP.getValueAt(i, 1));
            inputSP.add(Double.parseDouble(bang_tim_info_SP.getValueAt(i, 2).toString()));
            inputSP.add(Double.parseDouble(bang_tim_info_SP.getValueAt(i, 3).toString()));
            inputSP.add(Integer.parseInt(bang_tim_info_SP.getValueAt(i, 4).toString()));
            test_soluong = SauVan.update_data(inputSP);
            if(test_soluong == -1)
            {
                thongbao_text("[Chưa cập nhập]File dữ liệu đang mở. đóng để cập nhập lại",Color.RED);
                row_Count = 0;
            }
            else if(test_soluong == -2)
            {
                row_Count = 0;
                thongbao_text("[Chưa cập nhập]Không tìm thấy file dữ liệu",Color.RED);
            }
        }

        if(row_Count != 0)
        {
            //clear data
            clear_table(dm_info_SP);
            clear_Updated_list();
            thongbao_text("Hoàn thành quá trình cập nhập:" ,Color.BLUE);
        }
        jScrollPane_banghoadon.setVisible(true);
        barCode_grabFocus();
    }//GEN-LAST:event_botton_update_databaseActionPerformed

    private void botton_add_prod2PaylistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botton_add_prod2PaylistActionPerformed
        // TODO add your handling code here:
        int tim_row_count = bang_tim_info_SP.getRowCount();
        Object them_MaSP2PAY;
        int index_sp;
        for(int i =0; i < tim_row_count; i++)
        {
            if((boolean)bang_tim_info_SP.getValueAt(i, 5))
            {
                them_MaSP2PAY = bang_tim_info_SP.getValueAt(i, 0);
                index_sp = SauVan.tim_sp(them_MaSP2PAY.toString());
                if(index_sp == -1)
                {
                    thongbao_text("Chưa có Mã sản phẩm trong dữ liệu: " + them_MaSP2PAY, Color.RED);
                }
                else
                {
                    addtopay(index_sp);
                }
            }
        }
        clear_table(dm_info_SP);
        clear_Updated_list();
        botton_add_prod2Paylist.setEnabled(false);
        jScrollPane_banghoadon.setVisible(true);
        barCode_grabFocus();
    }//GEN-LAST:event_botton_add_prod2PaylistActionPerformed

    private void field_tim_tenSPKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_field_tim_tenSPKeyReleased
        // TODO add your handling code here:
        String ten_sp = field_tim_tenSP.getText().trim();
        if(!ten_sp.isEmpty())
        {
            Vector array_result = SauVan.timThongMinh_sp(field_tim_tenSP.getText().trim());

            //clear table
            display_result2infoTable(array_result);

            if(array_result.size() > 0)
            {
                botton_add_prod2Paylist.setEnabled(true);
                botton_update_database.setEnabled(true);
            }
            else
            {
                botton_add_prod2Paylist.setEnabled(false);
                botton_update_database.setEnabled(false);
            }
        }
        else
        {
            //clear_table(dm_info_SP);
            int old_result_count = dm_info_SP.getRowCount();

            for(int i = old_result_count - 1; i >= 0; i --)
            {
                if(!(boolean)dm_info_SP.getValueAt(i, 5))
                {

                    dm_info_SP.removeRow(i);
                }
            }
            botton_add_prod2Paylist.setEnabled(false);
            botton_update_database.setEnabled(false);
        }
    }//GEN-LAST:event_field_tim_tenSPKeyReleased

    private void menu_danhbadtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_danhbadtActionPerformed
        // TODO add your handling code here:
        danhba.setVisible(true);
    }//GEN-LAST:event_menu_danhbadtActionPerformed

    private void menu_thoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_thoatActionPerformed
        // TODO add your handling code here:
        exit_program();
    }//GEN-LAST:event_menu_thoatActionPerformed

    private void menu_inmavachActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_inmavachActionPerformed
        // TODO add your handling code here:
        in_ma_vach();
    }//GEN-LAST:event_menu_inmavachActionPerformed

    private void menu_inhoadonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_inhoadonActionPerformed
        // TODO add your handling code here:
        in_hoa_don();
    }//GEN-LAST:event_menu_inhoadonActionPerformed

    private void menu_capnhapgiatenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_capnhapgiatenActionPerformed
        // TODO add your handling code here:
        cap_nhap_gia_ten();
    }//GEN-LAST:event_menu_capnhapgiatenActionPerformed

    private void menu_lamtuoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_lamtuoiActionPerformed
        // TODO add your handling code here:
        refresh();
    }//GEN-LAST:event_menu_lamtuoiActionPerformed

    private void menu_xemlaihoadonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_xemlaihoadonActionPerformed
        // TODO add your handling code here:
        xem_lai_hoa_don();
    }//GEN-LAST:event_menu_xemlaihoadonActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        setup_configuration();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        get_bill_configure();
        in_hoa_don.get_bill_configure();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void Button_luahoadonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_luahoadonActionPerformed
        // TODO add your handling code here:
        buttonSaveBill();
        barCode_grabFocus();
    }//GEN-LAST:event_Button_luahoadonActionPerformed

    private void ComboBox_SaveBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBox_SaveBillActionPerformed
        // TODO add your handling code here:
        LoadSavedBill();
        buttonSaveBillsetText(0);
        saveBillIdxNoti = 1;
        barCode_grabFocus();
    }//GEN-LAST:event_ComboBox_SaveBillActionPerformed
    public void display_text_paylist()
    {
        System.out.println("Size :" + Paid_MaSP.size() + "-" +
        Paid_TenSP.size() + "-" +     Paid_GiaLSP.size() + "-" +
        Paid_GiaSSP.size() + "-" +    Paid_SoLuongSP.size() + "-" +
        Paid_TongGiaSP.size() );
    }
    public void remove_element_paylist(int i)
    {
        Paid_MaSP.removeElementAt(i);
        Paid_TenSP.removeElementAt(i);
        Paid_GiaLSP.removeElementAt(i);
        Paid_GiaSSP.removeElementAt(i);
        Paid_SoLuongSP.removeElementAt(i);
        Paid_TongGiaSP.removeElementAt(i);               
    }
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
            System.out.println("ERROR: main : ClassNotFoundException");
        } catch (InstantiationException ex) {
            System.out.println("ERROR: main : InstantiationException");
        } catch (IllegalAccessException ex) {
            System.out.println("ERROR: main : IllegalAccessException");
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            System.out.println("ERROR: main : UnsupportedLookAndFeelException");
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main_form().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Button_luahoadon;
    private javax.swing.JButton Button_them;
    private javax.swing.JComboBox ComboBox_SaveBill;
    private javax.swing.JComboBox ComboBox_history;
    private javax.swing.JLabel Label_other_price;
    private javax.swing.JLabel Label_other_price1;
    private javax.swing.JMenu Menu_tool;
    private javax.swing.JTextField No_cu;
    private javax.swing.JLabel Thong_bao_text;
    private javax.swing.JTable bang_HoaDon;
    private javax.swing.JTable bang_tim_info_SP;
    private javax.swing.JButton botton_add_prod2Paylist;
    private javax.swing.JButton botton_clear_info_table;
    private javax.swing.JButton botton_history_tracking;
    private javax.swing.JButton botton_pay_print_bill;
    private javax.swing.JButton botton_print_barcode;
    private javax.swing.JButton botton_thanhtoan_mode;
    private javax.swing.JButton botton_update_database;
    private javax.swing.JButton botton_update_mode;
    private javax.swing.JLabel date_time;
    private javax.swing.JLabel date_time1;
    private javax.swing.JTextField field_barcode_maSP;
    private javax.swing.JTextField field_price_other;
    private javax.swing.JTextField field_return_money;
    private javax.swing.JTextField field_tim_MaSP;
    private javax.swing.JTextField field_tim_tenSP;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel_hoa_don;
    private javax.swing.JPanel jPanel_thongTin;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane_banghoadon;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JMenuItem menu_capnhapgiaten;
    private javax.swing.JMenuItem menu_danhbadt;
    private javax.swing.JMenu menu_file;
    private javax.swing.JMenuItem menu_inhoadon;
    private javax.swing.JMenuItem menu_inmavach;
    private javax.swing.JMenuItem menu_lamtuoi;
    private javax.swing.JMenuItem menu_thoat;
    private javax.swing.JMenuItem menu_xemlaihoadon;
    private javax.swing.JCheckBox select_price;
    private javax.swing.JTextField ten_khach_hang;
    // End of variables declaration//GEN-END:variables

    private URL getResource(String myimagejpg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
