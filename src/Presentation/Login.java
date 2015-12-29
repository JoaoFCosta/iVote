/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Presentation;

import javax.swing.JOptionPane;
import Business.SGE;
import Business.Eleicao;
import Business.EleicaoPresidencial;
import java.util.Collection;
import javax.swing.DefaultListModel;

/**
 *
 * @authors joaocosta,zcbg
 */
public class Login extends javax.swing.JFrame {

  private final SGE sge;

  /**
   * Creates new form Login
   */
  public Login () {
    initComponents();
    this.sge = new SGE();
    this.setTitle("iVote");
    this.setListaEleicoes(sge.eleicoes());
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

      jTabbedPane1 = new javax.swing.JTabbedPane();
      jPanel3 = new javax.swing.JPanel();
      jPanel1 = new javax.swing.JPanel();
      cardIdTextField = new javax.swing.JTextField();
      jPanel2 = new javax.swing.JPanel();
      passwordField = new javax.swing.JPasswordField();
      loginButton = new javax.swing.JButton();
      jPanel4 = new javax.swing.JPanel();
      jPanel5 = new javax.swing.JPanel();
      jScrollPane1 = new javax.swing.JScrollPane();
      electionsList = new javax.swing.JList();
      resultsButton = new javax.swing.JButton();

      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
      setMinimumSize(new java.awt.Dimension(800, 600));
      setPreferredSize(new java.awt.Dimension(800, 600));

      jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Nº Cartão de Identificação"));

      javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
      jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(
          jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(cardIdTextField)
            .addContainerGap())
          );
      jPanel1Layout.setVerticalGroup(
          jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(cardIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 6, Short.MAX_VALUE))
          );

      jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Password"));

      javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
      jPanel2.setLayout(jPanel2Layout);
      jPanel2Layout.setHorizontalGroup(
          jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(passwordField)
            .addContainerGap())
          );
      jPanel2Layout.setVerticalGroup(
          jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel2Layout.createSequentialGroup()
            .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 6, Short.MAX_VALUE))
          );

      loginButton.setText("Login");
      loginButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
          loginButtonActionPerformed(evt);
        }
      });

      javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
      jPanel3.setLayout(jPanel3Layout);
      jPanel3Layout.setHorizontalGroup(
          jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 688, Short.MAX_VALUE)
                .addComponent(loginButton))
              .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
          );
      jPanel3Layout.setVerticalGroup(
          jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 385, Short.MAX_VALUE)
            .addComponent(loginButton)
            .addContainerGap())
          );

      jTabbedPane1.addTab("Votação", jPanel3);

      jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Eleições"));

      jScrollPane1.setViewportView(electionsList);

      javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
      jPanel5.setLayout(jPanel5Layout);
      jPanel5Layout.setHorizontalGroup(
          jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel5Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE)
            .addContainerGap())
          );
      jPanel5Layout.setVerticalGroup(
          jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel5Layout.createSequentialGroup()
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE)
            .addContainerGap())
          );

      resultsButton.setText("Consultar Resultados");
      resultsButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
          resultsButtonActionPerformed(evt);
        }
      });

      javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
      jPanel4.setLayout(jPanel4Layout);
      jPanel4Layout.setHorizontalGroup(
          jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel4Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(resultsButton)))
            .addContainerGap())
          );
      jPanel4Layout.setVerticalGroup(
          jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel4Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(resultsButton)
            .addContainerGap())
          );

      jTabbedPane1.addTab("Resultados", jPanel4);

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
          layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jTabbedPane1)
          );
      layout.setVerticalGroup(
          layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jTabbedPane1))
          );

      pack();
    }// </editor-fold>//GEN-END:initComponents

  /**
   * Método que corre quando o botão de resultados é pressionado.
   */
  private void resultsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resultsButtonActionPerformed
    // Aceder à eleição selecionada.
    Eleicao selecao = (Eleicao) electionsList.getModel().getElementAt(
      electionsList.getSelectedIndex());

    if (selecao instanceof EleicaoPresidencial) {
      ResultadoPresidencial RP = new ResultadoPresidencial();
      RP.setLocationRelativeTo(this);
      this.dispose();
      RP.setVisible(true);
    } else {
      ResultadoLegislativaGeral RL = new ResultadoLegislativaGeral();
      RL.setLocationRelativeTo(this);
      this.dispose();
      RL.setVisible(true);
    }
  }//GEN-LAST:event_resultsButtonActionPerformed

  /** Popula a lista de eleições do separador 'Resultados' com a lista de
   *  eleições fornecida utilizando o método 'toString()'.
   *  @param lista Lista de eleições a utilizar.
   */
  private void setListaEleicoes (Collection<Eleicao> lista) {
    DefaultListModel<Eleicao> dlm = new DefaultListModel<Eleicao>();

    lista.stream().forEach((e) -> {
      dlm.addElement(e);
    });

    electionsList.setModel(dlm);
  }

  /* -------------- LOGIN -------------- */

  /** Botão de Login pressionado. */
  private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
    
    String ccidadao       = cardIdTextField.getText();
    String password       = passwordField.getText();
    boolean votoEfetuado=false;
    
    /** Caso em que input não é inserido. */
    if (ccidadao.equals("") || password.equals("")) {
      JOptionPane.showMessageDialog(this,
          "O campo de cartão de cidadão e password não pode estar vazio.");
    }
    /** Caso em que input é inserido. */
    else{
        /** Login de administrador. */
        if (ccidadao.charAt(0) == 'a') {
            loginAdmin(ccidadao, password);
        } 
        /** Login de eleitor. */
        else{
            /**  Verificar se os campos de username ou password estão vazios
              *  e verificar se o eleitor ainda não votou. */
            votoEfetuado = sge.votoEfetuado(Integer.parseInt(ccidadao));
            if (votoEfetuado) {
                JOptionPane.showMessageDialog(this, 
                    "O eleitor já efectou o seu voto nestas Eleições.");
            }
            /** Caso o eleitor ainda não tenha votado. */
            else{
                loginEleitor(ccidadao, password);
            }
        }
    }
  }//GEN-LAST:event_loginButtonActionPerformed

  private void loginEleitor(String ccidadao, String password) {
    // Verificar se os dados estão correctos.
    boolean r           = sge.loginEleitor(Integer.parseInt(ccidadao), password);
    //0 ou outro qualquer caso->Não, 1->Presidencial, 2->Legislativa
    int eleicaoAberta   = 1;
    int idEleicao       = sge.idMaisRecenteEleicao();
    int idCidadao       = Integer.parseInt(ccidadao);

    if (r) {
        if(eleicaoAberta==1){
            int ronda = sge.rondaMaisRecente(idEleicao);
            VotoPresidenciais VP = new VotoPresidenciais(sge, idEleicao, idCidadao, ronda);
            VP.setLocationRelativeTo(this);
            this.dispose();
            VP.setVisible(true);
        }
        if(eleicaoAberta==2){
            VotoLegislativas VL = new VotoLegislativas(sge, idEleicao, idCidadao);
            VL.setLocationRelativeTo(this);
            this.dispose();
            VL.setVisible(true);
        }
        if(eleicaoAberta!=1 && eleicaoAberta!=2){
            JOptionPane.showMessageDialog(this,
            "Não existe nenhuma Eleição Aberta.");
        }
    } else {
      JOptionPane.showMessageDialog(this,
          "Dados inválidos.");
    }
  }

  private void loginAdmin(String id, String password) {
    // Verificar se os dados estão correctos.
    boolean r = sge.loginAdministrador(id, password);

    if (r) {
      MenuAdministrador MA  = new MenuAdministrador();
      MA.setLocationRelativeTo(this);
      this.dispose();
      MA.setVisible(true);
    } else {
      JOptionPane.showMessageDialog(this,
          "Dados inválidos.");
    }
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    try {
      javax.swing.UIManager.setLookAndFeel(
          javax.swing.UIManager.getSystemLookAndFeelClassName());
    } catch (Exception ex) {
      System.out.println("Unable to load native look and feel");
    }

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        // Criar a frame e centrar no ecrã.
        Login login = new Login();
        login.setVisible(true);
        login.setLocationRelativeTo(null);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTextField cardIdTextField;
  private javax.swing.JList electionsList;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JPanel jPanel4;
  private javax.swing.JPanel jPanel5;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JTabbedPane jTabbedPane1;
  private javax.swing.JButton loginButton;
  private javax.swing.JPasswordField passwordField;
  private javax.swing.JButton resultsButton;
  // End of variables declaration//GEN-END:variables
}
