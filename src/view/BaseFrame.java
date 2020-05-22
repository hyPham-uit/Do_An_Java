/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;


import com.placeholder.PlaceHolder;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import model.BenhNhan;
import model.NhanVien;
//import com.sun.imageio.plugins.png.RowFilter;
//import java.lang.Object;
//import javax.swing.RowFilter;
//import javax.swing.table.TableRowSorter;


/**
 *
 * @author MyPC
 */
public class BaseFrame extends javax.swing.JFrame {
    int location=-1;
    PlaceHolder p1, p2, p3;
    public int getlocation(){
        return location;
    }

    
    ArrayList<NhanVien> listNV= nhanVienList();
    
    private DefaultTableModel tblModelNhanVien;
    public ArrayList<NhanVien> getList(){
        return listNV;
    }
    
    //lấy nhân viên sau khi tìm kiếm
    public NhanVien getNhanVien(){
        return listNV.get(location);
    }
    
    private Connection conn;
    public void connect(){
        try {
            String url="jdbc:oracle:thin:@localhost:1521:orcl";
            conn=DriverManager.getConnection(url,"DOAN_ORACLE","admin");
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
            //(DefaultTableModel)tblNhanVien.getModel();
//    public void connect(){
//        String url="jdbc:oracle:thin:@localhost:1521:orcl";
//        try{
//            conn=DriverManager.getConnection(url,"DOAN_ORACLE","minhhy");
//        }catch(SQLException e){
//            e.printStackTrace();
//        }
//    }
    
    //tìm kiếm vị trí nhân viên
    public int searchNhanVien(ArrayList<NhanVien> arr, long target) {
        for(int i=0; i<arr.size(); i++){
            int x=arr.get(i).getMaNV();
            if (x==target) {
                return i;
            }
        }
        return -1;
    }
    
    //nhập các nhân viên từ database vào list
    public ArrayList<NhanVien> nhanVienList(){
        ArrayList<NhanVien> nhanViensList=new ArrayList<>();
        String url="jdbc:oracle:thin:@localhost:1521:orcl";
        try{
            Connection conn=DriverManager.getConnection(url,"DOAN_ORACLE","admin");
            String query1="SELECT * FROM NHANVIEN";
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery(query1);
            NhanVien nv;
            while(rs.next()){
                Date ngaySinh=rs.getDate("NGAYSINH");
                //Date date=(Date) new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(ngaySinh);
                DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");  
                String strDate = dateFormat.format(ngaySinh);  
                
//                nv=new NhanVien(rs.getInt("MANV"), rs.getString("HOTEN"), rs.getString("NGAYSINH"),rs.getString("DIACHI"),rs.getString("GIOITINH"),
//                        rs.getString("SDT"), rs.getString("CHUCDANH"), rs.getString("MAPHONG"), rs.getLong("LUONG"));
                nv=new NhanVien(rs.getInt("MANV"), rs.getString("HOTEN"),strDate,rs.getString("DIACHI"),rs.getString("GIOITINH"),
                    rs.getString("SDT"), rs.getString("CHUCDANH"), rs.getString("MAPHONG"), rs.getLong("LUONG"));
                nhanViensList.add(nv);
            }
            conn.close();
        }catch(SQLException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không connect được database");
        }
//        catch (ParseException ex) {
//            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return nhanViensList;
        //conn.close();
    }
    
    //sau khi nhập database và list, ta fetch data vào table để hiển thị ra màn hình
    public void show_nhanVien(){
        tblModelNhanVien=(DefaultTableModel)tblNhanVien.getModel();
        //tblNhanVien.setModel(tblModelNhanVien);
        int j=1;
        for(int i=0; i<listNV.size();i++){
            Object[] row={j, listNV.get(i).getMaNV(),listNV.get(i).getTenNV(),listNV.get(i).getNgSinh(),listNV.get(i).getDiaChi(),
                listNV.get(i).getGioiTinh(),listNV.get(i).getSoDT(),listNV.get(i).getChucDanh(),listNV.get(i).getMaPhong(),listNV.get(i).getLuong()};
            tblModelNhanVien.addRow(row);
            j++;
        }
        setVisible(true);
    }
    
    //sau khi thêm thông tin nhân viên ở insertNhanVienDialog, ta insert dữ liệu đó vào list và database, sau đó add nhân viên vào table
    public int addNhanVien(){
        try {
            DefaultTableModel tblModelNhanVien=(DefaultTableModel)tblNhanVien.getModel();
            int i=listNV.size()-1;
            int STT=listNV.size();
            int MaNV=listNV.get(i).getMaNV();
            String TenNV=listNV.get(i).getTenNV();
            //String NgSinh=listNV.get(i).getNgSinh();
            String NgSinh=listNV.get(i).getNgSinh();
            
            String date=listNV.get(i).getNgSinh();
            
            java.util.Date date2 = new SimpleDateFormat("MMMM d, yyyy").parse(date);
            java.sql.Date sqlDate = new java.sql.Date(date2.getTime());
            
            String DiaChi=listNV.get(i).getDiaChi();
            String GioiTinh=listNV.get(i).getGioiTinh();
            String SoDT=listNV.get(i).getSoDT();
            String ChucDanh=listNV.get(i).getChucDanh();
            int MaPhong=Integer.parseInt(listNV.get(i).getMaPhong());
            long Luong=listNV.get(i).getLuong();
            Object[] objs={STT, MaNV, TenNV, NgSinh, DiaChi, GioiTinh ,SoDT, ChucDanh, MaPhong,Luong};
            
            //thêm vô csdl
            connect();
            String insert="insert into NHANVIEN values(?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement pre=conn.prepareStatement(insert);
            int phanQuyen=1;
            if(ChucDanh.equalsIgnoreCase("nhan vien"))
                phanQuyen=1;
            else if (ChucDanh.equalsIgnoreCase("quan ly"))
                phanQuyen=2;
            else{
                JOptionPane.showMessageDialog(panelKhamBenh, "Nhập chức danh là nhan vien hoặc quan ly");
                return 0;
            }
            
            //Nhập dữ liệu vô csdl
            pre.setInt(1, MaNV);
            pre.setString(2, TenNV);
            pre.setDate(3, sqlDate);
            pre.setString(4, GioiTinh);
            pre.setString(5, DiaChi);
            pre.setString(6, SoDT);
            pre.setString(7, "");
            pre.setString(8, "");
            pre.setInt(9, phanQuyen);
            pre.setString(10, ChucDanh);
            pre.setInt(11, MaPhong);
            pre.setLong(12, Luong);
            int x=pre.executeUpdate();
            JOptionPane.showMessageDialog(panelKhamBenh, x+" dòng đã được thêm vào csdl");
            
            tblModelNhanVien.addRow(objs);
            conn.close();
            return 1;
        }catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi kết nối csdl");
            return 0;
        }catch (ParseException ex){
            //Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi convert dữ liệu Ngày sinh");
            return 0;
        }
    }
    
    public int deleteNhanVien(){
        try {
            tblModelNhanVien.removeRow(location);
            listNV.remove(location);
            location=-1;
//            String url="jdbc:oracle:thin:@localhost:1521:orcl";
//            Connection conn=DriverManager.getConnection(url,"DOAN_ORACLE","admin");
            connect();
            String delete="delete from NHANVIEN where MANV="+txtTimMaNV.getText().toString();
            Statement stm=conn.createStatement();
            int x=stm.executeUpdate(delete);
            txtTimMaNV.setText(""); 
            conn.close();
            return x;
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi kết nối csdl");
            return 0;
        }  
    }
    
    public int deleteNhanVienMouseClick(int manv){
        try {
            tblModelNhanVien.removeRow(location);
            listNV.remove(location);
            location=-1;
//            String url="jdbc:oracle:thin:@localhost:1521:orcl";
//            Connection conn=DriverManager.getConnection(url,"DOAN_ORACLE","admin");
            connect();
            //String delete="delete from NHANVIEN where MANV="+txtTimMaNV.getText().toString();
            String delete="delete from NHANVIEN where MANV="+manv;
            Statement stm=conn.createStatement();
            int x=stm.executeUpdate(delete);
            //txtTimMaNV.setText(""); 
            conn.close();
            return x;
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi kết nối csdl");
            return 0;
        }  
    }
    
    public void updateNhanVienJtable(int loca){
        tblModelNhanVien.setValueAt(listNV.get(loca).getTenNV(), loca, 2);
        tblModelNhanVien.setValueAt(listNV.get(loca).getNgSinh(), loca, 3);
        tblModelNhanVien.setValueAt(listNV.get(loca).getDiaChi(), loca, 4);
        tblModelNhanVien.setValueAt(listNV.get(loca).getGioiTinh(), loca, 5);
        tblModelNhanVien.setValueAt(listNV.get(loca).getSoDT(), loca, 6);
        tblModelNhanVien.setValueAt(listNV.get(loca).getChucDanh(), loca, 7);
        tblModelNhanVien.setValueAt(listNV.get(loca).getMaPhong(), loca, 8);
        tblModelNhanVien.setValueAt(listNV.get(loca).getLuong(), loca, 9);
    }
    
    public int updateNhanVien(){
        try {
            DefaultTableModel tblModelNhanVien=(DefaultTableModel)tblNhanVien.getModel();
            //Gán thông tin cho biến
            int i=location;
            int STT=listNV.size();
            int MaNV=listNV.get(i).getMaNV();
            String TenNV=listNV.get(i).getTenNV();
            //String NgSinh=listNV.get(i).getNgSinh();
            String NgSinh=listNV.get(i).getNgSinh();
            
            String date=listNV.get(i).getNgSinh();
            java.util.Date date2 = new SimpleDateFormat("MMMM d, yyyy").parse(date);
            java.sql.Date sqlDate = new java.sql.Date(date2.getTime());
            
            String DiaChi=listNV.get(i).getDiaChi();
            String GioiTinh=listNV.get(i).getGioiTinh();
            
            String SoDT=listNV.get(i).getSoDT();
            String ChucDanh=listNV.get(i).getChucDanh();
            int MaPhong=Integer.parseInt(listNV.get(i).getMaPhong());
            long Luong=listNV.get(i).getLuong();
            
            //kết nối csdl
//            String url="jdbc:oracle:thin:@localhost:1521:orcl";
//            Connection conn=DriverManager.getConnection(url,"DOAN_ORACLE","admin");
            connect();
            //update dtb
            String update="update NHANVIEN set HOTEN=?, NGAYSINH=?, GIOITINH=?, DIACHI=?,"
                    + "SDT=?, USERNAME=?, PASSWORD=?, PHANQUYEN=?, CHUCDANH=?, MAPHONG=?,"
                    + "LUONG=? WHERE MANV="+MaNV;
            PreparedStatement pre=conn.prepareStatement(update);
            int phanQuyen=1;
            if(ChucDanh.equalsIgnoreCase("nhan vien"))
                phanQuyen=1;
            else if (ChucDanh.equalsIgnoreCase("quan ly"))
                phanQuyen=2;
            else{
                JOptionPane.showMessageDialog(panelKhamBenh, "Nhập chức danh là nhan vien hoặc quan ly");
                return 0;
            }
            
            //Nhập dữ liệu vô csdl
            
            pre.setString(1, TenNV);
            pre.setDate(2, sqlDate);
            pre.setString(3, GioiTinh);
            pre.setString(4, DiaChi);
            pre.setString(5, SoDT);
            pre.setString(6, "");
            pre.setString(7, "");
            pre.setInt(8, phanQuyen);
            pre.setString(9, ChucDanh);
            pre.setInt(10, MaPhong);
            pre.setLong(11, Luong);
            //pre.setInt(12, MaNV);
            int x=pre.executeUpdate();
            JOptionPane.showMessageDialog(panelKhamBenh, x+" dòng đã được cập nhật");
            
            //tblModelNhanVien.addRow(objs);
            conn.close();
            //update Jtable
            updateNhanVienJtable(location);
            location=-1;
            return 1;
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi kết nối csdl");
            return 0;
        } catch (ParseException ex){
            //Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi convert dữ liệu Ngày sinh");
            return 0;
        }
    }
    
    /**
     * Creates new form JFrame
     */
   
    //lấy hình từ folder src/images để chèn hình vào frame
    public void editImageFrame(){
        ImageIcon myimage = new ImageIcon("src/images/da1.png");
        Image img1 = myimage.getImage();
        Image img2=myimage.getImage().getScaledInstance(jLabel1.getWidth(), jLabel1.getHeight(), Image.SCALE_SMOOTH);
        jLabel1.setIcon(new ImageIcon(img2));
        
        ImageIcon myimage1 = new ImageIcon("src/images/da2.png");
        Image img3 = myimage1.getImage().getScaledInstance(jLabel3.getWidth(), jLabel3.getHeight(), Image.SCALE_SMOOTH);
        //ImageIcon i1 = new ImageIcon(img4);
        jLabel3.setIcon(new ImageIcon(img3));
        
        ImageIcon myimage2 = new ImageIcon("src/images/da3.png");
        Image img5 = myimage2.getImage().getScaledInstance(jLabel4.getWidth(), jLabel4.getHeight(), Image.SCALE_SMOOTH);
        //ImageIcon i2 = new ImageIcon(img6);
        jLabel4.setIcon(new ImageIcon(img5));
    }
    
    
    
    //------------Tiếp nhận----------------------------------------------------------------------------
    //private Connection conn;
    private DefaultTableModel tblModel;
    ArrayList<BenhNhan> listBN = getBenhNhanList(); 
        
    public ArrayList<BenhNhan> getBenhNhanList(){
        ArrayList<BenhNhan> BenhNhansList = new ArrayList<>();
        try{
            connect();
            String query2 = "SELECT * FROM BENHNHAN";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query2);
            BenhNhan bn;
            while(rs.next()){          
                Date ngaySinh = rs.getDate("NGAYSINH");
                DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");  
                String NgaySinh = dateFormat.format(ngaySinh); 
                bn = new BenhNhan(rs.getInt("MABN"),rs.getString("HOTEN"),NgaySinh,rs.getString("GIOITINH"),rs.getString("DIACHI"),rs.getString("SDT"));
                BenhNhansList.add(bn);
            }
            conn.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return BenhNhansList;
    }
   
    
    public void addBenhNhan(BenhNhan s){
        listBN.add(s);
        tblModel = (DefaultTableModel)tblBN.getModel();
        tblModel.setRowCount(0);
        int i = 1;
        for(BenhNhan bn: listBN){
            tblModel.addRow(new Object[]{i,bn.getMaBN(),bn.getHoTen(),bn.getNgaySinh(),bn.getGioiTinh(),bn.getDiaChi(),bn.getSDT()});
            i++;
        }
    }
   
    public void show_BenhNhan(){
        ArrayList<BenhNhan> list = getBenhNhanList();
        tblModel =(DefaultTableModel)tblBN.getModel();
        int j = 1;
        for(int i=0; i<listBN.size(); i++){
            Object[] row={j, listBN.get(i).getMaBN(), listBN.get(i).getHoTen(), listBN.get(i).getNgaySinh(), 
                         listBN.get(i).getGioiTinh(), listBN.get(i).getDiaChi(), listBN.get(i).getSDT()};
            tblModel.addRow(row);
            j++;
        }
        int id=1;
        boolean flag;
        while(true){
            flag=false;
            for(BenhNhan nv:listBN){
                if(id==nv.getMaBN()){
                    flag=true;
                    break;
                }
            }
            if(!flag) break;
            ++id;
        }
        txtMaBN.setText(id+"");
        txtMaBN.setEditable(false);
        
        p1=new PlaceHolder(txtTenBN, "Nguyễn Văn A");
        p2=new PlaceHolder(txtDiaChi, "khu phố 6, phường Linh Trung, quận Thủ Đức");
        p3=new PlaceHolder(txtSDT, "0781234567");
        
        setVisible(true);
    }
    
    //---------------------hàm khởi tạo mặc định------------------------------
    //hàm khởi tạo mặc định
    public BaseFrame() {
        initComponents();
              
        Border jpanel_title_border = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black);
        jPanel_title.setBorder(jpanel_title_border);
        //chèn hình
        editImageFrame();
        setTitle("Quản lí phòng mạch tư");
        show_nhanVien();
        show_BenhNhan();
        setVisible(true);
        //txtTimMaNV.setEditable(false);
    }
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PopUpMenu = new javax.swing.JPopupMenu();
        delete = new javax.swing.JMenuItem();
        show = new javax.swing.JMenuItem();
        update = new javax.swing.JMenuItem();
        jPanel_title = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel_minimize = new javax.swing.JLabel();
        jLabel_close = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButtonBenhNhan = new javax.swing.JButton();
        jButtonThuoc = new javax.swing.JButton();
        jButtonTiepNhan = new javax.swing.JButton();
        jButtonKhamBenh = new javax.swing.JButton();
        jButtonNhanvien = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        panelNhanVien = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblNhanVien = new javax.swing.JTable();
        btThemNV = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        btTimNV = new javax.swing.JButton();
        txtTimMaNV = new javax.swing.JTextField();
        panelBenhNhan = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        panelThuoc = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        panelToaThuoc = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        panelKhamBenh = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jButtonToaThuoc = new javax.swing.JButton();
        panelTiepNhan = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtTim = new javax.swing.JTextField();
        btThemPK = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBN = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtMaBN = new javax.swing.JTextField();
        txtTenBN = new javax.swing.JTextField();
        txtDiaChi = new javax.swing.JTextField();
        txtSDT = new javax.swing.JTextField();
        txtNgaySinh = new com.toedter.calendar.JDateChooser();
        jMale = new javax.swing.JRadioButton();
        jFemale = new javax.swing.JRadioButton();
        btThemBN = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();

        delete.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        delete.setText("Xóa");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });
        PopUpMenu.add(delete);

        show.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        show.setText("Xem thông tin");
        show.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showActionPerformed(evt);
            }
        });
        PopUpMenu.add(show);

        update.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        update.setText("Sửa ");
        update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateActionPerformed(evt);
            }
        });
        PopUpMenu.add(update);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel_title.setBackground(new java.awt.Color(153, 153, 255));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("QUẢN LÍ PHÒNG MẠCH TƯ");

        jLabel3.setText("jLabel3");

        jLabel4.setText("jLabel4");

        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout jPanel_titleLayout = new javax.swing.GroupLayout(jPanel_title);
        jPanel_title.setLayout(jPanel_titleLayout);
        jPanel_titleLayout.setHorizontalGroup(
            jPanel_titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_titleLayout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 231, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(118, 118, 118)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel_titleLayout.setVerticalGroup(
            jPanel_titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel3)
            .addGroup(jPanel_titleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel_minimize.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel_minimize.setText(" -");
        jLabel_minimize.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel_close.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel_close.setText("X");
        jLabel_close.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        jButtonBenhNhan.setText("BỆNH NHÂN");
        jButtonBenhNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBenhNhanActionPerformed(evt);
            }
        });

        jButtonThuoc.setText("THUỐC");
        jButtonThuoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonThuocActionPerformed(evt);
            }
        });

        jButtonTiepNhan.setText("TIẾP NHẬN");
        jButtonTiepNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTiepNhanActionPerformed(evt);
            }
        });

        jButtonKhamBenh.setText("KHÁM BỆNH");
        jButtonKhamBenh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonKhamBenhActionPerformed(evt);
            }
        });

        jButtonNhanvien.setText("NHÂN VIÊN");
        jButtonNhanvien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNhanvienActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jButtonNhanvien)
                .addGap(60, 60, 60)
                .addComponent(jButtonBenhNhan)
                .addGap(60, 60, 60)
                .addComponent(jButtonThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addComponent(jButtonTiepNhan)
                .addGap(60, 60, 60)
                .addComponent(jButtonKhamBenh, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonNhanvien, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(jButtonBenhNhan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonThuoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonKhamBenh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonTiepNhan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLayeredPane1.setLayout(new java.awt.CardLayout());

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel8.setText("DANH SÁCH NHÂN VIÊN");

        tblNhanVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã NV", "Họ và tên", "Ngày sinh", "Địa chỉ", "Giới tính", "SĐT", "Chức danh", "Mã phòng", "Lương"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblNhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblNhanVienMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblNhanVien);
        if (tblNhanVien.getColumnModel().getColumnCount() > 0) {
            tblNhanVien.getColumnModel().getColumn(0).setMinWidth(20);
            tblNhanVien.getColumnModel().getColumn(0).setPreferredWidth(20);
            tblNhanVien.getColumnModel().getColumn(1).setPreferredWidth(30);
            tblNhanVien.getColumnModel().getColumn(2).setPreferredWidth(135);
            tblNhanVien.getColumnModel().getColumn(2).setMaxWidth(140);
            tblNhanVien.getColumnModel().getColumn(3).setPreferredWidth(40);
            tblNhanVien.getColumnModel().getColumn(4).setPreferredWidth(160);
            tblNhanVien.getColumnModel().getColumn(4).setMaxWidth(200);
            tblNhanVien.getColumnModel().getColumn(5).setPreferredWidth(30);
            tblNhanVien.getColumnModel().getColumn(6).setPreferredWidth(40);
            tblNhanVien.getColumnModel().getColumn(7).setPreferredWidth(40);
            tblNhanVien.getColumnModel().getColumn(8).setPreferredWidth(30);
            tblNhanVien.getColumnModel().getColumn(9).setPreferredWidth(45);
        }

        btThemNV.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btThemNV.setText("Thêm nhân viên");
        btThemNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThemNVActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Mã nhân viên");

        btTimNV.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btTimNV.setText("Tìm kiếm");
        btTimNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTimNVActionPerformed(evt);
            }
        });

        txtTimMaNV.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTimMaNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimMaNVActionPerformed(evt);
            }
        });
        txtTimMaNV.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimMaNVKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout panelNhanVienLayout = new javax.swing.GroupLayout(panelNhanVien);
        panelNhanVien.setLayout(panelNhanVienLayout);
        panelNhanVienLayout.setHorizontalGroup(
            panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNhanVienLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelNhanVienLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 893, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(panelNhanVienLayout.createSequentialGroup()
                        .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8)
                            .addGroup(panelNhanVienLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTimMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btTimNV)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btThemNV)
                        .addGap(28, 28, 28))))
        );
        panelNhanVienLayout.setVerticalGroup(
            panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNhanVienLayout.createSequentialGroup()
                .addComponent(jLabel8)
                .addGap(6, 6, 6)
                .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btTimNV)
                    .addComponent(jLabel5)
                    .addComponent(btThemNV)
                    .addComponent(txtTimMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLayeredPane1.add(panelNhanVien, "card2");

        jLabel9.setText("BỆNH NHÂN PANEL");

        javax.swing.GroupLayout panelBenhNhanLayout = new javax.swing.GroupLayout(panelBenhNhan);
        panelBenhNhan.setLayout(panelBenhNhanLayout);
        panelBenhNhanLayout.setHorizontalGroup(
            panelBenhNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBenhNhanLayout.createSequentialGroup()
                .addGap(267, 267, 267)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(553, Short.MAX_VALUE))
        );
        panelBenhNhanLayout.setVerticalGroup(
            panelBenhNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBenhNhanLayout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(235, Short.MAX_VALUE))
        );

        jLayeredPane1.add(panelBenhNhan, "card3");

        jLabel6.setText("thuốc");

        javax.swing.GroupLayout panelThuocLayout = new javax.swing.GroupLayout(panelThuoc);
        panelThuoc.setLayout(panelThuocLayout);
        panelThuocLayout.setHorizontalGroup(
            panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelThuocLayout.createSequentialGroup()
                .addContainerGap(613, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(251, 251, 251))
        );
        panelThuocLayout.setVerticalGroup(
            panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelThuocLayout.createSequentialGroup()
                .addGap(99, 99, 99)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(203, Short.MAX_VALUE))
        );

        jLayeredPane1.add(panelThuoc, "card4");

        jLabel10.setText("TOA THUOC PANEL");

        javax.swing.GroupLayout panelToaThuocLayout = new javax.swing.GroupLayout(panelToaThuoc);
        panelToaThuoc.setLayout(panelToaThuocLayout);
        panelToaThuocLayout.setHorizontalGroup(
            panelToaThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelToaThuocLayout.createSequentialGroup()
                .addGap(382, 382, 382)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(382, Short.MAX_VALUE))
        );
        panelToaThuocLayout.setVerticalGroup(
            panelToaThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelToaThuocLayout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(jLabel10)
                .addContainerGap(229, Short.MAX_VALUE))
        );

        jLayeredPane1.add(panelToaThuoc, "card5");

        jLabel7.setText("KHÁM BỆNH PANEL");

        jButtonToaThuoc.setText("TOA THUỐC");
        jButtonToaThuoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonToaThuocActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelKhamBenhLayout = new javax.swing.GroupLayout(panelKhamBenh);
        panelKhamBenh.setLayout(panelKhamBenhLayout);
        panelKhamBenhLayout.setHorizontalGroup(
            panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKhamBenhLayout.createSequentialGroup()
                .addContainerGap(640, Short.MAX_VALUE)
                .addComponent(jButtonToaThuoc)
                .addGap(184, 184, 184))
        );
        panelKhamBenhLayout.setVerticalGroup(
            panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonToaThuoc, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                .addGap(226, 226, 226))
        );

        jLayeredPane1.add(panelKhamBenh, "card7");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel12.setText("DANH SÁCH PHIẾU KHÁM");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setText("TÌM KIẾM BỆNH NHÂN");

        txtTim.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTim.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTim.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKeyReleased(evt);
            }
        });

        btThemPK.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btThemPK.setText("Thêm phiếu khám");
        btThemPK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThemPKActionPerformed(evt);
            }
        });

        tblBN.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã bệnh nhân", "Tên bệnh nhân", "Ngày Sinh", "Giới tính", "Địa chỉ", "SĐT"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblBN);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setText("THÔNG TIN BỆNH NHÂN ĐĂNG KÝ KHÁM");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel15.setText("Mã bệnh nhân");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel16.setText("Tên bệnh nhân");

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel17.setText("Ngày Sinh");

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel18.setText("Giới tính");

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel19.setText("Địa chỉ");

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel20.setText("SĐT");

        jMale.setText("MALE");

        jFemale.setText("FEMALE");

        btThemBN.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btThemBN.setText("Thêm bệnh nhân");
        btThemBN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThemBNActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel21.setText("Tìm kiếm");
        jLabel21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout panelTiepNhanLayout = new javax.swing.GroupLayout(panelTiepNhan);
        panelTiepNhan.setLayout(panelTiepNhanLayout);
        panelTiepNhanLayout.setHorizontalGroup(
            panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTiepNhanLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTiepNhanLayout.createSequentialGroup()
                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelTiepNhanLayout.createSequentialGroup()
                                .addGap(389, 389, 389)
                                .addComponent(jLabel21)
                                .addGap(18, 18, 18)
                                .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtTim, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btThemPK, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jScrollPane2))
                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelTiepNhanLayout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelTiepNhanLayout.createSequentialGroup()
                                        .addComponent(jLabel15)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtMaBN))
                                    .addGroup(panelTiepNhanLayout.createSequentialGroup()
                                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel16)
                                            .addComponent(jLabel17))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtSDT)
                                            .addComponent(txtDiaChi)
                                            .addComponent(txtNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtTenBN)))
                                    .addGroup(panelTiepNhanLayout.createSequentialGroup()
                                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel20)
                                            .addGroup(panelTiepNhanLayout.createSequentialGroup()
                                                .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel19)
                                                    .addComponent(jLabel18))
                                                .addGap(53, 53, 53)
                                                .addComponent(jMale)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jFemale)))
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(panelTiepNhanLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btThemBN))))
                    .addGroup(panelTiepNhanLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel14)
                        .addGap(48, 48, 48)))
                .addGap(35, 35, 35))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTiepNhanLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addGap(220, 220, 220))
        );
        panelTiepNhanLayout.setVerticalGroup(
            panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTiepNhanLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btThemPK)
                    .addComponent(jLabel15)
                    .addComponent(txtMaBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel21))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16)
                        .addComponent(txtTenBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTiepNhanLayout.createSequentialGroup()
                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel17)
                            .addComponent(txtNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(jMale)
                            .addComponent(jFemale))
                        .addGap(18, 18, 18)
                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btThemBN))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        jLayeredPane1.add(panelTiepNhan, "card6");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 4, Short.MAX_VALUE)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_title, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel_minimize, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_close)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_minimize)
                    .addComponent(jLabel_close))
                .addGap(0, 0, 0)
                .addComponent(jPanel_title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonThuocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonThuocActionPerformed
        // TODO add your handling code here:
        panelBenhNhan.setVisible(false);
        panelNhanVien.setVisible(false);
        panelKhamBenh.setVisible(false);
        panelTiepNhan.setVisible(false);
        panelThuoc.setVisible(true);
        panelToaThuoc.setVisible(false);
    }//GEN-LAST:event_jButtonThuocActionPerformed

    private void jButtonBenhNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBenhNhanActionPerformed
        // TODO add your handling code here:
        panelBenhNhan.setVisible(true);
        panelNhanVien.setVisible(false);
        panelKhamBenh.setVisible(false);
        panelTiepNhan.setVisible(false);
        panelThuoc.setVisible(false);
        panelToaThuoc.setVisible(false);
    }//GEN-LAST:event_jButtonBenhNhanActionPerformed

    private void jButtonToaThuocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonToaThuocActionPerformed
        // TODO add your handling code here:
        panelBenhNhan.setVisible(false);
        panelNhanVien.setVisible(false);
        panelKhamBenh.setVisible(false);
        panelTiepNhan.setVisible(false);
        panelThuoc.setVisible(false);
        panelToaThuoc.setVisible(true);
    }//GEN-LAST:event_jButtonToaThuocActionPerformed

    private void jButtonNhanvienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNhanvienActionPerformed
        // TODO add your handling code here:
        panelBenhNhan.setVisible(false);
        panelNhanVien.setVisible(true);
        panelKhamBenh.setVisible(false);
        panelTiepNhan.setVisible(false);
        panelThuoc.setVisible(false);
        panelToaThuoc.setVisible(false);
    }//GEN-LAST:event_jButtonNhanvienActionPerformed

    private void jButtonTiepNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTiepNhanActionPerformed
        // TODO add your handling code here:
        panelBenhNhan.setVisible(false);
        panelNhanVien.setVisible(false);
        panelKhamBenh.setVisible(false);
        panelTiepNhan.setVisible(true);
        panelThuoc.setVisible(false);
        panelToaThuoc.setVisible(false);
    }//GEN-LAST:event_jButtonTiepNhanActionPerformed

    private void jButtonKhamBenhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonKhamBenhActionPerformed
        // TODO add your handling code here:
        panelBenhNhan.setVisible(false);
        panelNhanVien.setVisible(false);
        panelKhamBenh.setVisible(true);
        panelTiepNhan.setVisible(false);
        panelThuoc.setVisible(false);
        panelToaThuoc.setVisible(false);
    }//GEN-LAST:event_jButtonKhamBenhActionPerformed

    private void btTimNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTimNVActionPerformed
        // TODO add your handling code here:
        String manv=txtTimMaNV.getText()+"";
        if(manv.equals(""))
            JOptionPane.showMessageDialog(panelKhamBenh, "Vui lòng nhập mã nhân viên để tìm kiếm");
        else{
            //JOptionPane.showMessageDialog(panelKhamBenh, manv);
            long x=Integer.parseInt(manv);
                if(searchNhanVien(listNV, x)>=0){
                    location=searchNhanVien(listNV, x);
                    showNhanVienDialog show=new showNhanVienDialog(this, rootPaneCheckingEnabled);
                    //JOptionPane.showMessageDialog(panelKhamBenh, "Tồn tại");
                    show.setVisible(rootPaneCheckingEnabled);
                }
                else{
                    JOptionPane.showMessageDialog(this, "Không tồn tại nhân viên!");
                }
                txtTimMaNV.setText("");
        }
    }//GEN-LAST:event_btTimNVActionPerformed

    private void btThemNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThemNVActionPerformed
        // TODO add your handling code here:
        insertNhanVienJDialog insertNV= new insertNhanVienJDialog(this, rootPaneCheckingEnabled);
        insertNV.setVisible(true);
    }//GEN-LAST:event_btThemNVActionPerformed

    private void txtTimMaNVKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimMaNVKeyReleased
        // TODO add your handling code here:
        DefaultTableModel table= (DefaultTableModel)tblNhanVien.getModel();
        String search= txtTimMaNV.getText().toUpperCase();
        TableRowSorter<DefaultTableModel> tr=new TableRowSorter<DefaultTableModel>(table);
        tblNhanVien.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(search));
        
    }//GEN-LAST:event_txtTimMaNVKeyReleased

    private void txtTimMaNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimMaNVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimMaNVActionPerformed

    private void tblNhanVienMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNhanVienMouseReleased
        // TODO add your handling code here:
        if(evt.getButton()==MouseEvent.BUTTON3){
            if(evt.isPopupTrigger()&&tblNhanVien.getSelectedRowCount()!=0){
                PopUpMenu.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_tblNhanVienMouseReleased

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        // TODO add your handling code here:
        int row=tblNhanVien.getSelectedRow();
        
        if(row!=-1){
            location=row;
            int manv=Integer.parseInt(tblNhanVien.getValueAt(row, 1).toString());
            String name=tblNhanVien.getValueAt(row, 2).toString();
            int response=JOptionPane.showConfirmDialog(rootPane, "Bạn có chắc muốn xóa "+name+" không ?", "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(response==JOptionPane.YES_OPTION){
                if(deleteNhanVienMouseClick(manv)==1){
                        JOptionPane.showMessageDialog(rootPane, "Nhân viên "+name+" đã bị xóa");
                }
                else{
                    JOptionPane.showMessageDialog(panelKhamBenh, "Xóa không thành công!");
                }
            }
        }
        location=-1;
    }//GEN-LAST:event_deleteActionPerformed

    private void showActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showActionPerformed
        // TODO add your handling code here:
        int row=tblNhanVien.getSelectedRow();
        location=row;
        showNhanVienDialog show=new showNhanVienDialog(this, rootPaneCheckingEnabled);
        //JOptionPane.showMessageDialog(panelKhamBenh, "Tồn tại");
        show.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_showActionPerformed

    private void updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateActionPerformed
        // TODO add your handling code here:
        int row=tblNhanVien.getSelectedRow();
        location=row;
        showNhanVienDialog show=new showNhanVienDialog(this, rootPaneCheckingEnabled);
        show.setVisible(rootPaneCheckingEnabled);
        //location=-1;
    }//GEN-LAST:event_updateActionPerformed

    private void txtTimKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKeyReleased
        DefaultTableModel table = (DefaultTableModel) tblBN.getModel();
        String search = txtTim.getText().toUpperCase();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(table);
        tblBN.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(search));
    }//GEN-LAST:event_txtTimKeyReleased

    private void btThemPKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThemPKActionPerformed
        //InsertPhieuKhamJDialog insertPK = new InsertPhieuKhamJDialog(this, rootPaneCheckingEnabled);
        //insertPK.setVisible(true);
        InserpkJdialog insertPK = new InserpkJdialog(this, rootPaneCheckingEnabled);
        insertPK.setVisible(true);
    }//GEN-LAST:event_btThemPKActionPerformed

    private void btThemBNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThemBNActionPerformed
        int maBN = 0;
        boolean flag = true;

        if(txtMaBN.getText().equals("")||txtTenBN.getText().equals("")||txtNgaySinh.getDate().equals("")||
            txtDiaChi.getText().equals("")||txtSDT.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Bạn hãy điền đầy đủ thông tin");
        }
        else{
            try{

                try{
                    maBN = Integer.parseInt(txtMaBN.getText());
                }catch(Exception e){
                    JOptionPane.showMessageDialog(rootPane, "Mã nhân viên phải là số và không chứa kí tự khác");
                    flag = false;
                }

                String tenBN = txtTenBN.getText();
                DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
                String ngaySinh = dateFormat.format(txtNgaySinh.getDate());
                java.util.Date date2 = new SimpleDateFormat("MMM d, yyyy").parse(ngaySinh);
                java.sql.Date sqlDate = new java.sql.Date(date2.getTime());

                String gioiTinh = "";
                if(jMale.isSelected()){
                    gioiTinh += "MALE";
                }
                if(jFemale.isSelected()){
                    gioiTinh += "FEMALE";
                }

                String diaChi = txtDiaChi.getText();
                String soDT = txtSDT.getText();

                if(flag == true){
                    BenhNhan bn = new BenhNhan(maBN,tenBN,ngaySinh,gioiTinh,diaChi,soDT);
                    addBenhNhan(bn);
                    JOptionPane.showMessageDialog(this, "Thêm thành công");

                    java.util.Date today = new java.util.Date();
                    txtMaBN.setText("");
                    txtTenBN.setText("");
                    txtNgaySinh.setDate(today);
                    txtDiaChi.setText("");
                    txtSDT.setText("");
                }
                String url = "jdbc:oracle:thin:@localhost:1521:orcl";
                try {
                    conn = DriverManager.getConnection(url,"DOAN_ORACLE","admin");
                    String insert = "insert into BENHNHAN values(?,?,?,?,?,?)";
                    PreparedStatement st = conn.prepareStatement(insert);
                    st.setInt(1, maBN);
                    st.setString(2, tenBN);
                    st.setDate(3, sqlDate);
                    st.setString(4, gioiTinh);
                    st.setString(5, diaChi);
                    st.setString(6, soDT);
                    int a = st.executeUpdate();

                    JOptionPane.showMessageDialog(this, "THEM THANH CONG "+a+" dòng");
                } catch (SQLException ex) {
                    Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this, "Lỗi csdl");
                }

            }catch (ParseException ex){
                JOptionPane.showMessageDialog(this, "Lỗi convert dữ liệu Ngày sinh");
            }

        }

    }//GEN-LAST:event_btThemBNActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws ClassNotFoundException, SQLException{
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(() -> {
//            new JFrame().setVisible(true);
//        });
        
//        Connection con=DriverManager.getConnection(
//                "jdbc:oracle:thin:@localhost:1521:orcl","DOAN_ORACLE","minhhy");
//        //Connection con=DriverManger
//        Statement stmt=con.createStatement();
//        //thực hiện câu truy vấn
//        ResultSet rs=stmt.executeQuery("select * from NHANVIEN");
//        while(rs.next())
//            System.out.print(rs.getInt(1)+"\t"+ rs.getString(2)+"\t"+ rs.getString(3)+"\t"+ rs.getString(4)+"\t"+ 
//                        rs.getString(5)+"\t"+ rs.getString(6)+"\t"+ rs.getString(7)+"\t"+ rs.getString(8)+"\t"+ rs.getLong(9));
//        con.close();
        BaseFrame frame=new BaseFrame();
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu PopUpMenu;
    private javax.swing.JButton btThemBN;
    private javax.swing.JButton btThemNV;
    private javax.swing.JButton btThemPK;
    private javax.swing.JButton btTimNV;
    private javax.swing.JMenuItem delete;
    private javax.swing.JButton jButtonBenhNhan;
    private javax.swing.JButton jButtonKhamBenh;
    private javax.swing.JButton jButtonNhanvien;
    private javax.swing.JButton jButtonThuoc;
    private javax.swing.JButton jButtonTiepNhan;
    private javax.swing.JButton jButtonToaThuoc;
    private javax.swing.JRadioButton jFemale;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_close;
    private javax.swing.JLabel jLabel_minimize;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JRadioButton jMale;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel_title;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel panelBenhNhan;
    private javax.swing.JPanel panelKhamBenh;
    private javax.swing.JPanel panelNhanVien;
    private javax.swing.JPanel panelThuoc;
    private javax.swing.JPanel panelTiepNhan;
    private javax.swing.JPanel panelToaThuoc;
    private javax.swing.JMenuItem show;
    private javax.swing.JTable tblBN;
    private javax.swing.JTable tblNhanVien;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtMaBN;
    private com.toedter.calendar.JDateChooser txtNgaySinh;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTenBN;
    private javax.swing.JTextField txtTim;
    private javax.swing.JTextField txtTimMaNV;
    private javax.swing.JMenuItem update;
    // End of variables declaration//GEN-END:variables
}
