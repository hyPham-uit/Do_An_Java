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
    private DefaultTableModel tblModelNhanVien;
    int location=-1;
    PlaceHolder p1, p2, p3;
    PlaceHolder timKiemNV;

    private Connection conn;
    //kết nối csdl
    public void connect(){
        try {
            String url="jdbc:oracle:thin:@localhost:1521:orcl";
            conn=DriverManager.getConnection(url,"DOAN_ORACLE","admin");
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Không connect được database");
        }
    }
    
//-------------------------------------------------------------------------Nhân viên----------------------------------------------------------------------------------------
    ArrayList<NhanVien> listNV= nhanVienList();
    public int getlocation(){
        return location;
    }
    public ArrayList<NhanVien> getList(){
        return listNV;
    }
    //lấy nhân viên sau khi tìm kiếm
    public NhanVien getNhanVien(){
        return listNV.get(location);
    }
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
        try{
            connect();
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
        }
        return nhanViensList;
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
            connect();
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
    
    //sau khi nhập database và list, ta fetch data vào table để hiển thị ra màn hình
    public void show_nhanVien(){
        tblModelNhanVien=(DefaultTableModel)tblNhanVien.getModel();
        int j=1;
        for(int i=0; i<listNV.size();i++){
            Object[] row={j, listNV.get(i).getMaNV(),listNV.get(i).getTenNV(),listNV.get(i).getNgSinh(),listNV.get(i).getDiaChi(),
                listNV.get(i).getGioiTinh(),listNV.get(i).getSoDT(),listNV.get(i).getChucDanh(),listNV.get(i).getMaPhong(),listNV.get(i).getLuong()};
            tblModelNhanVien.addRow(row);
            j++;
        }
        timKiemNV=new PlaceHolder(txtTimMaNV, "Tìm kiếm thông tin NV");
        setVisible(true);
    }
    
//--------------------------------------------------------------------------Tiếp nhận----------------------------------------------------------------------------
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
    
//lấy hình từ folder src/images để chèn hình vào frame---------------------------------------------------------------------------------------------------------------
    public void editImageFrame(){
        ImageIcon myimage = new ImageIcon("src/images/da1.png");
        Image img1 = myimage.getImage();
        Image img2=myimage.getImage().getScaledInstance(jLabel1.getWidth(), jLabel1.getHeight(), Image.SCALE_SMOOTH);
        jLabel1.setIcon(new ImageIcon(img2));
        
//        ImageIcon myimage1 = new ImageIcon("src/images/da2.png");
//        Image img3 = myimage1.getImage().getScaledInstance(jLabel3.getWidth(), jLabel3.getHeight(), Image.SCALE_SMOOTH);
//        //ImageIcon i1 = new ImageIcon(img4);
//        jLabel3.setIcon(new ImageIcon(img3));
//        
//        ImageIcon myimage2 = new ImageIcon("src/images/da3.png");
//        Image img5 = myimage2.getImage().getScaledInstance(jLabel4.getWidth(), jLabel4.getHeight(), Image.SCALE_SMOOTH);
//        //ImageIcon i2 = new ImageIcon(img6);
//        jLabel4.setIcon(new ImageIcon(img5));
        
        ImageIcon imgExit= new ImageIcon("src/images/exit.png");
        Image imgExit1=imgExit.getImage().getScaledInstance(btExit.getWidth(), btExit.getHeight(), Image.SCALE_SMOOTH);
    }
    
//--------------------------------------------------------------------Khám bệnh-----------------------------------------------------------------------------------------------
    private int tienKham=30000;

    public int getTienKham() {
        return tienKham;
    }

    public void setTienKham(int tienKham) {
        this.tienKham = tienKham;
    }
    
    public void updateComboBoxBacSi(){
        try {
            connect();
            String s="NHAN VIEN";
            String query="SELECT * FROM NHANVIEN WHERE CHUCDANH='NHAN VIEN'";
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery(query);
            while(rs.next()){
                cbbBacSi.addItem(rs.getString("HOTEN"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(panelKhamBenh, "FAULT COMBOBOX BAC SI");
        }
    }
    
    public void updateComboboxChuanDoan(){
        try {
            connect();
            String query="SELECT DISTINCT LOAITHUOC FROM THUOC";
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery(query);
            while(rs.next()){
                cbbChuanDoan.addItem(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(panelKhamBenh, "FAULT COMBOBOX CHUAN DOAN");
        }
    }
    
    public void show_khamBenh(){
        txtMaBNKhamBenh.setEditable(false);
        txtMaHD.setEditable(false);
        txtTienKham.setEditable(false);
        txtTienThuoc.setEditable(false);
        updateComboBoxBacSi();
        updateComboboxChuanDoan();
        txtTienKham.setText(tienKham+"");
    }
//---------------------------------------------------------------------hàm khởi tạo mặc định----------------------------------------------------------------------------------
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
        show_khamBenh();
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
        update = new javax.swing.JMenuItem();
        jPanel_title = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
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
        txtTimMaNV = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        panelBenhNhan = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        panelKhamBenh = new javax.swing.JPanel();
        btToaThuoc = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtHoTenBN = new javax.swing.JTextField();
        txtLiDoKham = new javax.swing.JTextField();
        txtTrieuChung = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtMaHD = new javax.swing.JTextField();
        cbbChuanDoan = new javax.swing.JComboBox<>();
        cbbBacSi = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        txtMaBNKhamBenh = new javax.swing.JTextField();
        btThemBenhAn = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        txtTienKham = new javax.swing.JTextField();
        txtTienThuoc = new javax.swing.JTextField();
        txtThanhTien = new javax.swing.JTextField();
        btNextPatients = new javax.swing.JButton();
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
        panelThuoc1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        txtTimMaThuoc = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbThuoc = new javax.swing.JTable();
        jLabel33 = new javax.swing.JLabel();
        btThemThuoc = new javax.swing.JButton();
        btTimMatoa = new javax.swing.JButton();
        txtTimMatoa = new javax.swing.JTextField();
        btExit = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        delete.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        delete.setText("Xóa");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });
        PopUpMenu.add(delete);

        update.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        update.setText("Sửa ");
        update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateActionPerformed(evt);
            }
        });
        PopUpMenu.add(update);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel_title.setBackground(new java.awt.Color(69, 123, 157));
        jPanel_title.setForeground(new java.awt.Color(69, 123, 157));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("QUẢN LÍ PHÒNG MẠCH TƯ");

        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout jPanel_titleLayout = new javax.swing.GroupLayout(jPanel_title);
        jPanel_title.setLayout(jPanel_titleLayout);
        jPanel_titleLayout.setHorizontalGroup(
            jPanel_titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_titleLayout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel_titleLayout.setVerticalGroup(
            jPanel_titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
            .addGroup(jPanel_titleLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setForeground(new java.awt.Color(204, 204, 255));

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
                .addGap(15, 15, 15)
                .addComponent(jButtonTiepNhan)
                .addGap(63, 63, 63)
                .addComponent(jButtonKhamBenh, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62)
                .addComponent(jButtonBenhNhan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                .addComponent(jButtonThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63)
                .addComponent(jButtonNhanvien)
                .addGap(15, 15, 15))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButtonBenhNhan, jButtonKhamBenh, jButtonNhanvien, jButtonThuoc, jButtonTiepNhan});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonTiepNhan)
                        .addComponent(jButtonKhamBenh)
                        .addComponent(jButtonBenhNhan))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonThuoc)
                            .addComponent(jButtonNhanvien, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonBenhNhan, jButtonKhamBenh, jButtonNhanvien, jButtonThuoc, jButtonTiepNhan});

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

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Tìm kiếm");

        javax.swing.GroupLayout panelNhanVienLayout = new javax.swing.GroupLayout(panelNhanVien);
        panelNhanVien.setLayout(panelNhanVienLayout);
        panelNhanVienLayout.setHorizontalGroup(
            panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelNhanVienLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelNhanVienLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelNhanVienLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 950, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(panelNhanVienLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(txtTimMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btThemNV)
                        .addGap(28, 28, 28))))
        );
        panelNhanVienLayout.setVerticalGroup(
            panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNhanVienLayout.createSequentialGroup()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btThemNV)
                    .addComponent(txtTimMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
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
                .addContainerGap(610, Short.MAX_VALUE))
        );
        panelBenhNhanLayout.setVerticalGroup(
            panelBenhNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBenhNhanLayout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(235, Short.MAX_VALUE))
        );

        jLayeredPane1.add(panelBenhNhan, "card3");

        btToaThuoc.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btToaThuoc.setText("TOA THUỐC");
        btToaThuoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btToaThuocActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã PK", "Mã BN", "Họ và tên", "Lí do khám"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(30);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(30);
        }

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel11.setText("DANH SÁCH BỆNH NHÂN CHỜ KHÁM");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setText("THÔNG TIN BỆNH ÁN");

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel22.setText("Họ tên BN");

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel23.setText("Lí do khám");

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel24.setText("Triệu chứng");

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel25.setText("Chuẩn đoán");

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel26.setText("Bác sĩ khám");

        txtHoTenBN.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtLiDoKham.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtTrieuChung.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel27.setText("Mã hóa đơn");

        txtMaHD.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        cbbChuanDoan.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbbChuanDoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbChuanDoanActionPerformed(evt);
            }
        });

        cbbBacSi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel28.setText("Mã BN");

        txtMaBNKhamBenh.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        btThemBenhAn.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btThemBenhAn.setText("THÊM BỆNH ÁN");
        btThemBenhAn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThemBenhAnActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel29.setText("Tiền khám");

        jLabel30.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel30.setText("Tiền thuốc");

        jLabel31.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel31.setText("Thành tiền");

        txtTienKham.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtTienThuoc.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtThanhTien.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        btNextPatients.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btNextPatients.setText("BỆNH NHÂN KẾ TIẾP");

        javax.swing.GroupLayout panelKhamBenhLayout = new javax.swing.GroupLayout(panelKhamBenh);
        panelKhamBenh.setLayout(panelKhamBenhLayout);
        panelKhamBenhLayout.setHorizontalGroup(
            panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKhamBenhLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelKhamBenhLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addGap(194, 194, 194))
                    .addGroup(panelKhamBenhLayout.createSequentialGroup()
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(52, 52, 52)
                                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(btNextPatients))
                        .addGap(18, 18, 18)
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKhamBenhLayout.createSequentialGroup()
                                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTrieuChung, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtLiDoKham, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtHoTenBN, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btThemBenhAn)
                                    .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(cbbBacSi, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cbbChuanDoan, javax.swing.GroupLayout.Alignment.LEADING, 0, 150, Short.MAX_VALUE)))
                                .addGap(37, 37, 37))
                            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                .addComponent(txtMaBNKhamBenh, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(142, 142, 142)))
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btToaThuoc)
                            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                                    .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTienThuoc)
                                    .addComponent(txtThanhTien)))
                            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTienKham, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(126, 126, 126))))
        );
        panelKhamBenhLayout.setVerticalGroup(
            panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelKhamBenhLayout.createSequentialGroup()
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28)
                            .addComponent(txtMaBNKhamBenh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(txtHoTenBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtLiDoKham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23))
                        .addGap(16, 16, 16)
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel24)
                            .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtTrieuChung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel29)
                                .addComponent(txtTienKham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(16, 16, 16)
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(cbbChuanDoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel30)
                            .addComponent(txtTienThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbbBacSi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26)
                            .addComponent(jLabel31)
                            .addComponent(txtThanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btToaThuoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btThemBenhAn)
                    .addComponent(btNextPatients))
                .addGap(32, 32, 32))
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

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel6.setText("DANH SÁCH THUỐC");

        jLabel32.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel32.setText("Mã thuốc");

        txtTimMaThuoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimMaThuocActionPerformed(evt);
            }
        });

        tbThuoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã thuốc", "Tên thuốc", "Đơn vị", "Đơn giá", "Ngày SX", "Hạn SD", "Nơi SX"
            }
        ));
        tbThuoc.setMaximumSize(new java.awt.Dimension(2147483647, 0));
        tbThuoc.setMinimumSize(new java.awt.Dimension(155, 0));
        tbThuoc.setPreferredSize(new java.awt.Dimension(570, 0));
        jScrollPane4.setViewportView(tbThuoc);

        jLabel33.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel33.setText("Mã toa thuốc");

        btThemThuoc.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btThemThuoc.setText("Thêm thuốc");
        btThemThuoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThemThuocActionPerformed(evt);
            }
        });

        btTimMatoa.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btTimMatoa.setText("Tìm");

        txtTimMatoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimMatoaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelThuoc1Layout = new javax.swing.GroupLayout(panelThuoc1);
        panelThuoc1.setLayout(panelThuoc1Layout);
        panelThuoc1Layout.setHorizontalGroup(
            panelThuoc1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelThuoc1Layout.createSequentialGroup()
                .addGroup(panelThuoc1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelThuoc1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane4))
                    .addGroup(panelThuoc1Layout.createSequentialGroup()
                        .addGap(322, 322, 322)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelThuoc1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(txtTimMaThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 399, Short.MAX_VALUE)
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTimMatoa, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btTimMatoa)
                        .addGap(48, 48, 48)
                        .addComponent(btThemThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelThuoc1Layout.setVerticalGroup(
            panelThuoc1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelThuoc1Layout.createSequentialGroup()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelThuoc1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelThuoc1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTimMaThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtTimMatoa, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelThuoc1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btThemThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btTimMatoa, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLayeredPane1.add(panelThuoc1, "card4");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_title, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btExit, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btExit, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addComponent(jPanel_title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE)
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
        panelThuoc1.setVisible(true);
    }//GEN-LAST:event_jButtonThuocActionPerformed

    private void jButtonBenhNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBenhNhanActionPerformed
        // TODO add your handling code here:
        panelBenhNhan.setVisible(true);
        panelNhanVien.setVisible(false);
        panelKhamBenh.setVisible(false);
        panelTiepNhan.setVisible(false);
        panelThuoc1.setVisible(false);
    }//GEN-LAST:event_jButtonBenhNhanActionPerformed

    private void btToaThuocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btToaThuocActionPerformed
        // TODO add your handling code here:
        insertToaThuocDialog toathuoc=new insertToaThuocDialog(this, rootPaneCheckingEnabled);
        toathuoc.setVisible(true);
    }//GEN-LAST:event_btToaThuocActionPerformed

    private void jButtonNhanvienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNhanvienActionPerformed
        // TODO add your handling code here:
        panelBenhNhan.setVisible(false);
        panelNhanVien.setVisible(true);
        panelKhamBenh.setVisible(false);
        panelTiepNhan.setVisible(false);
        panelThuoc1.setVisible(false);
    }//GEN-LAST:event_jButtonNhanvienActionPerformed

    private void jButtonTiepNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTiepNhanActionPerformed
        // TODO add your handling code here:
        panelBenhNhan.setVisible(false);
        panelNhanVien.setVisible(false);
        panelKhamBenh.setVisible(false);
        panelTiepNhan.setVisible(true);
        panelThuoc1.setVisible(false);
    }//GEN-LAST:event_jButtonTiepNhanActionPerformed

    private void jButtonKhamBenhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonKhamBenhActionPerformed
        // TODO add your handling code here:
        panelBenhNhan.setVisible(false);
        panelNhanVien.setVisible(false);
        panelKhamBenh.setVisible(true);
        panelTiepNhan.setVisible(false);
        panelThuoc1.setVisible(false);
    }//GEN-LAST:event_jButtonKhamBenhActionPerformed

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

    private void btThemBenhAnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThemBenhAnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btThemBenhAnActionPerformed

    private void cbbChuanDoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbChuanDoanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbbChuanDoanActionPerformed

    private void txtTimMaThuocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimMaThuocActionPerformed
        DefaultTableModel table= (DefaultTableModel)tbThuoc.getModel();
        String search= txtTimMaThuoc.getText().toUpperCase();
        TableRowSorter<DefaultTableModel> tr=new TableRowSorter<DefaultTableModel>(table);
        tbThuoc.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(search));
    }//GEN-LAST:event_txtTimMaThuocActionPerformed

    private void btThemThuocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThemThuocActionPerformed
        // TODO add your handling code here:
        insertThuocJDialog insertThuoc= new insertThuocJDialog(this, rootPaneCheckingEnabled);
        insertThuoc.setVisible(true);
    }//GEN-LAST:event_btThemThuocActionPerformed

    private void txtTimMatoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimMatoaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimMatoaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws ClassNotFoundException, SQLException{
        BaseFrame frame=new BaseFrame();
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu PopUpMenu;
    private javax.swing.JButton btExit;
    private javax.swing.JButton btNextPatients;
    private javax.swing.JButton btThemBN;
    private javax.swing.JButton btThemBenhAn;
    private javax.swing.JButton btThemNV;
    private javax.swing.JButton btThemPK;
    private javax.swing.JButton btThemThuoc;
    private javax.swing.JButton btTimMatoa;
    private javax.swing.JButton btToaThuoc;
    private javax.swing.JComboBox<String> cbbBacSi;
    private javax.swing.JComboBox<String> cbbChuanDoan;
    private javax.swing.JMenuItem delete;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonBenhNhan;
    private javax.swing.JButton jButtonKhamBenh;
    private javax.swing.JButton jButtonNhanvien;
    private javax.swing.JButton jButtonThuoc;
    private javax.swing.JButton jButtonTiepNhan;
    private javax.swing.JRadioButton jFemale;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
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
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JRadioButton jMale;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel_title;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel panelBenhNhan;
    private javax.swing.JPanel panelKhamBenh;
    private javax.swing.JPanel panelNhanVien;
    private javax.swing.JPanel panelThuoc1;
    private javax.swing.JPanel panelTiepNhan;
    private javax.swing.JTable tbThuoc;
    private javax.swing.JTable tblBN;
    private javax.swing.JTable tblNhanVien;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtHoTenBN;
    private javax.swing.JTextField txtLiDoKham;
    private javax.swing.JTextField txtMaBN;
    private javax.swing.JTextField txtMaBNKhamBenh;
    private javax.swing.JTextField txtMaHD;
    private com.toedter.calendar.JDateChooser txtNgaySinh;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTenBN;
    private javax.swing.JTextField txtThanhTien;
    private javax.swing.JTextField txtTienKham;
    private javax.swing.JTextField txtTienThuoc;
    private javax.swing.JTextField txtTim;
    private javax.swing.JTextField txtTimMaNV;
    private javax.swing.JTextField txtTimMaThuoc;
    private javax.swing.JTextField txtTimMatoa;
    private javax.swing.JTextField txtTrieuChung;
    private javax.swing.JMenuItem update;
    // End of variables declaration//GEN-END:variables
}
