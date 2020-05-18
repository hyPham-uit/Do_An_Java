package view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.BenhNhan;
import model.PhieuKham;

public class InserpkJdialog extends javax.swing.JDialog {

    private Connection conn;
    private BaseFrame home;
    public InserpkJdialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        home = (BaseFrame) parent;
        this.setTitle("Nhập thông tin phiếu khám");
        this.setLocationRelativeTo(null);
     
        TangMaPKTuDong();
    }

    ArrayList<PhieuKham> listPK = getPhieuKhamList(); 
        
    public ArrayList<PhieuKham> getPhieuKhamList(){
        ArrayList<PhieuKham> PhieuKhamsList = new ArrayList<>();
        String url = "jdbc:oracle:thin:@localhost:1521/orclpdb";
        try{
            conn = DriverManager.getConnection(url,"DOAN_ORACLE","admin");
            String query2 = "SELECT * FROM PHIEUKHAM";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query2);
            PhieuKham pk;
            while(rs.next()){          
                java.sql.Date ngayKham = rs.getDate("NGAYKHAM");
                DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");  
                String NgayKham = dateFormat.format(ngayKham); 
                pk = new PhieuKham(rs.getInt("MAPK"),rs.getInt("MAPHONG"),rs.getInt("MABN"),rs.getString("NGAYKHAM"));
                PhieuKhamsList.add(pk);
            }
            conn.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return PhieuKhamsList;
    }
    
    
    public void TangMaPKTuDong(){
        // xét id tăng tự động
        txtMaPK.setEditable(false);
        int id = 1;
        boolean flag;
        while(true){
            flag = false;
            for(PhieuKham pk: listPK){
                if(id == pk.getMaPK()){
                    flag = true;
                    break;
                }
            }
            if(!flag) break;
            ++id;
        }
        txtMaPK.setText(id+"");
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtMaPK = new javax.swing.JTextField();
        txtMaBN = new javax.swing.JTextField();
        txtMaPhong = new javax.swing.JTextField();
        btThem = new javax.swing.JButton();
        btThoat = new javax.swing.JButton();
        txtNgayKham = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel1.setText("NHẬP THÔNG TIN PHIẾU KHÁM");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Mã phiếu khám");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Mã phòng");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Mã bệnh nhân");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Ngày khám");

        btThem.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btThem.setText("Thêm");
        btThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThemActionPerformed(evt);
            }
        });

        btThoat.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btThoat.setText("Thoát");
        btThoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThoatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btThem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btThoat))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMaPK)
                            .addComponent(txtMaPhong)
                            .addComponent(txtMaBN)
                            .addComponent(txtNgayKham, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(101, 101, 101)
                        .addComponent(jLabel1)))
                .addContainerGap(103, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtMaPK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtMaPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtMaBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(txtNgayKham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(104, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btThem)
                            .addComponent(btThoat)))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btThoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThoatActionPerformed
        this.dispose();
    }//GEN-LAST:event_btThoatActionPerformed

    private void btThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThemActionPerformed
        int MaPK = 0;
        int MaPhong = 0;
        int MaBN = 0;
        String NgayKham = "";
        boolean flag = true;
        
        if(txtMaPK.getText().equals("")||txtMaBN.getText().equals("")||txtMaPhong.getText().equals("")||txtNgayKham.getDate().equals("")){
            JOptionPane.showMessageDialog(this, "Bạn hãy điền đầy đủ thông tin");
        }
        else{
            try {
                
                try {
                    MaPK = Integer.parseInt(txtMaPK.getText());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(rootPane, "Mã phiếu khám phải là số và không chứa kí tự khác");
                    flag = false;
                }
                
                try {
                    MaPhong = Integer.parseInt(txtMaPhong.getText());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(rootPane, "Mã phòng phải là số và không chứa kí tự khác");
                    flag = false;
                }
                
                try {
                    MaBN = Integer.parseInt(txtMaBN.getText());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(rootPane, "Mã bệnh nhân phải là số và không chứa kí tự khác");
                    flag = false;    
                }
                
                DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
                String ngayKham = dateFormat.format(txtNgayKham.getDate());
                java.util.Date date2 = new SimpleDateFormat("MMM d, yyyy").parse(ngayKham);
                java.sql.Date sqlDate = new java.sql.Date(date2.getTime());
                
                if(flag == true){
                    PhieuKham pk = new PhieuKham(MaPK,MaPhong,MaBN,NgayKham);                    
                    JOptionPane.showMessageDialog(this, "Thêm thành công");
                    
                    
                }
                    
                String url = "jdbc:oracle:thin:@localhost:1521/orclpdb";
                try {
                    conn = DriverManager.getConnection(url,"DOAN_ORACLE","admin");
                    String insert = "insert into PHIEUKHAM values(?,?,?,?)";
                    PreparedStatement st = conn.prepareStatement(insert);
                    st.setInt(1, MaPK);
                    st.setInt(2, MaBN);
                    st.setInt(3, MaPhong);
                    st.setDate(4, sqlDate);
                    
                    int x = st.executeUpdate();
                    if(x > 0){
                        JOptionPane.showMessageDialog(this, "Thêm thành công " +x+ " dòng");
                    }else{
                        JOptionPane.showMessageDialog(this, "Không thêm được vào dữ liệu");
                    }
                    
                    java.util.Date today = new java.util.Date();
                    TangMaPKTuDong();
                    txtMaBN.setText("");
                    txtMaPhong.setText("");
                    txtNgayKham.setDate(today);
                }catch (SQLException ex) {
                    Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
                }

            }catch (ParseException ex) {
                Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.dispose();
        }  
    }//GEN-LAST:event_btThemActionPerformed

    
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
            java.util.logging.Logger.getLogger(InserpkJdialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InserpkJdialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InserpkJdialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InserpkJdialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                InserpkJdialog dialog = new InserpkJdialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btThem;
    private javax.swing.JButton btThoat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField txtMaBN;
    private javax.swing.JTextField txtMaPK;
    private javax.swing.JTextField txtMaPhong;
    private com.toedter.calendar.JDateChooser txtNgayKham;
    // End of variables declaration//GEN-END:variables
}
