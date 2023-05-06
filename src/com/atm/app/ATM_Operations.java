package com.atm.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;



public class ATM_Operations  {

	public static void main(String[] args)
			throws NumberFormatException, IOException, SQLException, ClassNotFoundException, ParseException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("==============================================================================");
		System.out.println("=====================  WELCOME TO MYCITY BANK ATM ================================");
		System.out.println("==============================================================================");
		System.out.println("\t\t 1 --> USERS");
		System.out.println("\t\t 2 --> ADMIN");
		System.out.println("==============================================================================");
		System.out.println("Enter your choice:");
		int choice = Integer.parseInt(br.readLine());
	   
		if (choice == 1) {

			System.out.println("==============================================================================");
			System.out.println("===========================    LOGIN DETAILS  ================================");
			System.out.println("==============================================================================");

			System.out.print("\t Enter your username:");
			String userName = br.readLine();
			System.out.print("\t Enter your password:");
			String userPassword = br.readLine();
			try {
				Connection conn = Mysql_Connection.getConnection();
				PreparedStatement ps = conn.prepareStatement("select accPassword from Users where accUsername=?");
				ps.setString(1, userName);
				ResultSet result = ps.executeQuery();
				String password = null;
				boolean login = false;
				while (result.next()) {
					password = result.getString("accPassword");
					login = true;
				}
				if (password.equals(userPassword)) {
					System.out
							.println("==============================================================================");
					System.out
							.println("===========================   Login successful ================================");
					System.out
							.println("==============================================================================");
					System.out.println("============================== WELCOME " + userName.toUpperCase()
							+ " ===============================");
					System.out
							.println("==============================================================================");

				
					String status = "Y";
					do {
						
						System.out.println("\t\t  1 --> Quick Cash");
						System.out.println("\t\t  2 --> Withdraw Amount");
						System.out.println("\t\t  3 --> Fund Transfer");
						System.out.println("\t\t  4 --> Balance Enquiry");
						System.out.println("\t\t  5 --> Change Pin");
						System.out.println("\t\t  6 --> Mini Statement");
					
						

					//	System.out.println("\t\t  1 --> Deposit Amount");
					//	System.out.println("\t\t  2 --> Withdraw Amount");
					//	System.out.println("\t\t  3 --> Fund Transfer");
					//	System.out.println("\t\t  4 --> Balance Check");
					//	System.out.println("\t\t  5 --> Change Password");
						System.out.println("\t\t  6 --> Exit/Logout");
						System.out.println(
								"==============================================================================");
						System.out.print("Enter your choice:");
						int operationCode = Integer.parseInt(br.readLine());
						ResultSet res;
						switch (operationCode) {
						
						case 1:
							System.out.println("Enter the Quick Cash:");
							double quickCash = Double.parseDouble(br.readLine());
                            
							
							ps = conn.prepareStatement("select * from Users where accUsername=?");
				
							ps.setString(1, userName);
							res = ps.executeQuery();
							
							double existingBalance = 0.0;
							long id = 0;
							while (res.next()) {
								existingBalance = res.getDouble("accBalance");
								id = res.getLong("id");
								
								
								ps = conn.prepareStatement("select * from Users where accUsername=?");
								ps.setString(1, userName);
								res = ps.executeQuery();
								existingBalance = 0.0;
								id = 0;
								while (res.next()) {
									existingBalance = res.getDouble("accBalance");
									id = res.getLong("id");
							}

								existingBalance = existingBalance - quickCash ;

								ps = conn.prepareStatement("update Users set accBalance=? where accUsername=?");
								ps.setDouble(1, existingBalance);
								ps.setString(2, userName);
							
							System.out.println("Please collect your money");  
							
							if (ps.executeUpdate() > 0) {
								ps = conn.prepareStatement("insert into transactions values(?,?,?,?,?,?,?,?)");
								Timestamp timestamp = new Timestamp(System.currentTimeMillis());
								long transactionId = timestamp.getTime();
								

								ps.setLong(1, transactionId);
								ps.setDate(2, new Date(System.currentTimeMillis()));
								ps.setDouble(3, existingBalance);
								ps.setString(4, "withdraw");
								ps.setLong(5, id);
								ps.setLong(6, id);
								ps.setLong(7, id);
								ps.setLong(8, id);

								ps.executeUpdate();

								System.out.println(
										"==============================================================================");
								System.out.println("Balance Updated!!");
								System.out.println("New account balance is :" + existingBalance);
								System.out.println(
										"==============================================================================");

							}

							System.out.println("Do you want to continue?(Y/N)");
							status = br.readLine();

							if (status.equals("n") || status.equals("N")) {
								login = false;
							}
							 else  
					            {  
					                //show custom error message   
					                System.out.println("Insufficient Balance");  
					            }  
							}
							break;
						case 2:
							System.out.println("Enter the Card No:");
							double cardNo = Double.parseDouble(br.readLine());
							
							System.out.println("Enter the PIN:");
							double pin = Double.parseDouble(br.readLine());
							
							System.out.println("Enter the withdraw amount:");
							double withdrawAmount = Double.parseDouble(br.readLine());
							
							ps = conn.prepareStatement("select * from Users where accUsername=?");
							ps.setString(1, userName);
							res = ps.executeQuery();
							existingBalance = 0.0;
							id = 0;
							while (res.next()) {
								existingBalance = res.getDouble("accBalance");
								id = res.getLong("id");
							}

							existingBalance = existingBalance - withdrawAmount;

							ps = conn.prepareStatement("update Users set accBalance=? where accUsername=?");
							ps.setDouble(1, existingBalance);
							ps.setString(2, userName);

							if (ps.executeUpdate() > 0) {
								ps = conn.prepareStatement("insert into transactions values(?,?,?,?,?,?,?,?)");
								Timestamp timestamp = new Timestamp(System.currentTimeMillis());
								long transactionId = timestamp.getTime();

								ps.setLong(1, transactionId);
								ps.setDate(2, new Date(System.currentTimeMillis()));
								ps.setDouble(3, existingBalance);
								ps.setString(4, "withdraw");
								ps.setLong(5, id);
								ps.setLong(6, id);
								

								ps.executeUpdate();

								System.out.println(
										"==============================================================================");
								System.out.println("Balance Updated!!");
								System.out.println("New account balance is :" + existingBalance);
								System.out.println(
										"==============================================================================");

							}

							System.out.println("Do you want to continue?(Y/N)");
							status = br.readLine();

							if (status.equals("n") || status.equals("N")) {
								login = false;
							}

							

							break; 
						case 3:
							// Fund Transfer
							System.out.println("Enter the transaction amount:");
							double amount = Double.parseDouble(br.readLine());

							System.out.println("Enter the receiver account Id:");
							long receiverid = Long.parseLong(br.readLine());

							// fetching sender account balance
							
							ps = conn.prepareStatement("select * from Users where accUsername=?");
							ps.setString(1, userName);
							res = ps.executeQuery();
							double senderexistingBalance = 0.0;
							id = 0;
							while (res.next()) {
								senderexistingBalance = res.getDouble("accBalance");
								id = res.getLong("id");
							}

							// fetching receiver account balance
							
							ps = conn.prepareStatement("select * from Users where id=?");
							ps.setLong(1, receiverid);
							res = ps.executeQuery();
							double receiverExistingBalance = 0.0;
							long rcvrid = 0;
							while (res.next()) {
								receiverExistingBalance = res.getDouble("accBalance");
								rcvrid = res.getLong("id");
							}

							if (senderexistingBalance > amount && rcvrid != 0) {
								senderexistingBalance = senderexistingBalance - amount;
								receiverExistingBalance = receiverExistingBalance + amount;

								ps = conn.prepareStatement("update Users set accBalance=? where id=?");
								ps.setDouble(1, senderexistingBalance);
								ps.setLong(2, id);
								ps.executeUpdate();

								ps = conn.prepareStatement("update Users set accBalance=? where id=?");
								ps.setDouble(1, receiverExistingBalance);
								ps.setLong(2, rcvrid);
								ps.executeUpdate();

								ps = conn.prepareStatement("insert into transactions values(?,?,?,?,?,?,?,?)");
								Timestamp timestamp = new Timestamp(System.currentTimeMillis());
								long transactionId = timestamp.getTime();

								ps.setLong(1, transactionId);
								ps.setDate(2, new Date(System.currentTimeMillis()));
								ps.setDouble(3, amount);
								ps.setString(4, "transfer");
						     	ps.setLong(5, id);
								ps.setLong(6, id);
								ps.setLong(7, id);
								ps.setLong(8, id);

								if (ps.executeUpdate() > 0) {

									System.out.println(
											"==============================================================================");
									System.out.println("Transaction successful!!");
									System.out.println("New account balance is :" + senderexistingBalance);
									System.out.println(
											"==============================================================================");
								} else {
									System.out.println(
											"==============================================================================");
									System.out.println("Transaction failed!!");
									System.out.println(
											"==============================================================================");

								}

								System.out.println("Do you want to continue?(Y/N)");
								status = br.readLine();

								if (status.equals("n") || status.equals("N")) {
									login = false;
								}

							} else if (senderexistingBalance < amount) {
								System.out.println(
										"==============================================================================");
								System.out.println("Insufficient account balance!!");
								System.out.println(
										"==============================================================================");

							} else if (rcvrid == 0) {
								System.out.println(
										"==============================================================================");
								System.out.println("Invalid receiver id!!");
								System.out.println(
										"==============================================================================");

							}

							break;

						case 4:
							ps = conn.prepareStatement("select * from Users where accUsername=?");
							ps.setString(1, userName);
							res = ps.executeQuery();
							double balance = 0.0;
							while (res.next()) {
								balance = res.getDouble("accBalance");

							}
							System.out.println(
									"==============================================================================");
							System.out.println("Current account balance is :" + balance);
							System.out.println(
									"==============================================================================");
							System.out.println("Do you want to continue?(Y/N)");
							status = br.readLine();

							if (status.equals("n") || status.equals("N")) {
								login = false;
							}

							break;
						case 5:
							System.out.println("Enter the old Card PIN:");
							String oldPassword = br.readLine();

							System.out.println("Enter the new  Card PIN:");
							String newPassword = br.readLine();

							System.out.println("Re-enter the  Card PIN:");
							String rePassword = br.readLine();

							ps = conn.prepareStatement("select * from Users where accUsername=?");
							ps.setString(1, userName);

							res = ps.executeQuery();
							String existingPassword = null;
							while (res.next()) {
								existingPassword = res.getString("pin");
							}

							if (existingPassword.equals(oldPassword)) {
								if (newPassword.equals(rePassword)) {
									ps = conn.prepareStatement("update Users set pin=? where accUsername=?");
									ps.setString(1, newPassword);
									ps.setString(2, userName);

									if (ps.executeUpdate() > 0) {
										System.out.println(
												"==============================================================================");
										System.out.println("Password changed successfully!!");
										System.out.println(
												"==============================================================================");

									} else {
										System.out.println(
												"==============================================================================");
										System.out.println("Problem in password changed!!");
										System.out.println(
												"==============================================================================");

									}

								} else {
									System.out.println(
											"==============================================================================");
									System.out.println("New password and retype password must be same!!");
									System.out.println(
											"==============================================================================");

								}
							} else {
								System.out.println(
										"==============================================================================");
								System.out.println("Old password is wrong!!");
								System.out.println(
										"==============================================================================");

							}
							System.out.println("Do you want to continue?(Y/N)");
							status = br.readLine();

							if (status.equals("n") || status.equals("N")) {
								login = false;
							}
							break;

						case 6:
							login = false;
							break;

						default:
							System.out.println("Wrong Choice!!");
							break;

						}

					} while (login);
					System.out.println("==============================================================================");
					System.out.println("Bye. Have a nice day!!");
					System.out.println("==============================================================================");

				} else {
					System.out
							.println("==============================================================================");
					System.out
							.println("================================  Wrong password  ============================");
					System.out
							.println("==============================================================================");
				}
			} catch (Exception e) {
				System.out.println(e);
				System.out.println("==============================================================================");
				System.out.println("===========================  Wrong username/password  ========================");
				System.out.println("==============================================================================");

			}

		} else if (choice == 2) {

			System.out.println("==============================================================================");
			System.out.println("===========================    LOGIN DETAILS  ================================");
			System.out.println("==============================================================================");

			System.out.print("\t Enter your username:");
			String userName = br.readLine();
			System.out.print("\t Enter your password:");
			String userPassword = br.readLine();

			Connection conn = Mysql_Connection.getConnection();
			PreparedStatement ps = conn.prepareStatement("select * from admin where username=?");
			ps.setString(1, userName);
			ResultSet result = ps.executeQuery();
			String password = null;
			boolean login = false;
			while (result.next()) {
				password = result.getString("password");
				login = true;
			}

			if (password.equals(userPassword)) {

				String status = "y";
				System.out.println("==============================================================================");
				System.out.println("=============================   WELCOME ADMIN    =============================");
				System.out.println("==============================================================================");

				do {
					System.out.println("==============================================================================");
				//	System.out.println("\t\t  1 --> Open New Account");
				//	System.out.println("\t\t  2 --> Close Account");
				//	System.out.println("\t\t  3 --> View Transactions");
				//	System.out.println("\t\t  4 --> Change Password");
					
					System.out.println("\t\t  1 --> Add New Users");
					System.out.println("\t\t  2 --> Close Account");
					System.out.println("\t\t  3 --> View Transactions");
					
					System.out.println("\t\t  5 --> Exit/Logout");
					System.out.println("==============================================================================");

					System.out.println("Enter your choice:");
					int operation = Integer.parseInt(br.readLine());

					switch (operation) {
					case 1:
						System.out.println("Enter Users's full name:");
						String card = br.readLine();

						System.out.println("Enter Users's user name:");
						String uname = br.readLine();

						System.out.println("Enter Card No: ");
						password = br.readLine();

						System.out.println("Enter account password: ");
						password = br.readLine();

						System.out.println("Re-enter account password: ");
						String rePassword = br.readLine();

						System.out.println("Set account Id:");
						long id = Long.parseLong(br.readLine());

						
						System.out.println("Initial balance:");
						double balance = Double.parseDouble(br.readLine());

						ps = conn.prepareStatement("insert into Users values(?,?,?,?,?,?,)");
						ps.setLong(1, id);
						ps.setString(2, card);
						ps.setString(3, uname);
						ps.setString(4, password);
						ps.setDouble(5, balance);
						ps.setLong(6, id);
					
						

						if (ps.executeUpdate() > 0) {
							System.out.println(
									"==============================================================================");
							System.out.println("Account created successfully!!");
							System.out.println(
									"==============================================================================");

						}

						System.out.println("Do you want to continue?(Y/N)");
						status = br.readLine();

						if (status.equals("n") || status.equals("N")) {
							login = false;
						}
						break;

					case 2:
						System.out.println("Enter account id:");
						long id1 = Long.parseLong(br.readLine());

						ps = conn.prepareStatement("delete from Users where id=?");
						ps.setLong(1, id1);

						if (ps.executeUpdate() > 0) {
							System.out.println(
									"==============================================================================");
							System.out.println("Account closed successfully!!");
							System.out.println(
									"==============================================================================");

						} else {
							System.out.println(
									"==============================================================================");
							System.out.println("Problem in account closing!!");
							System.out.println(
									"==============================================================================");

						}
						System.out.println("Do you want to continue?(Y/N)");
					status = br.readLine();

						if (status.equals("n") || status.equals("N")) {
							login = false;
						}
						break;
					case 3:
						System.out.println("Transaction Id \t Date \t Amount \t Type");

						ps = conn.prepareStatement("select * from transactions");
						ResultSet transactions = ps.executeQuery();

						while (transactions.next()) {
							System.out.println(transactions.getLong("transactionId") + "\t"
									+ transactions.getDate("transactionDate")

									+ "\t" + transactions.getDouble("transactionAmount") + "\t"
									+ transactions.getString("transactionType"));
						}
						System.out.println("Do you want to continue?(Y/N)");
						status = br.readLine();

						if (status.equals("n") || status.equals("N")) {
							login = false;
						}
						break;
						
					case 4:
						System.out.println("Enter the old password:");
						String oldPassword = br.readLine();

						System.out.println("Enter the new password:");
						String newPassword = br.readLine();

						System.out.println("Re-enter the new password:");
						String repassword = br.readLine();

						ps = conn.prepareStatement("select * from admin where username=?");
						ps.setString(1, userName);

						result = ps.executeQuery();
						String existingPassword = null;
						while (result.next()) {
							existingPassword = result.getString("password");

						}

						if (existingPassword.equals(oldPassword)) {
							if (newPassword.equals(repassword)) {
								ps = conn.prepareStatement("update admin set password=? where username=?");
								ps.setString(1, newPassword);
								ps.setString(2, userName);

								if (ps.executeUpdate() > 0) {
									System.out.println(
											"==============================================================================");
									System.out.println("Password changed successfully!!");
									System.out.println(
											"==============================================================================");

								} else {
									System.out.println(
											"==============================================================================");
									System.out.println("Problem in password changed!!");
									System.out.println(
											"==============================================================================");

								}

							} else {
								System.out.println(
										"==============================================================================");
								System.out.println("New password and retype password must be same!!");
								System.out.println(
										"==============================================================================");

							}
						} else {
							System.out.println(
									"==============================================================================");
							System.out.println("Old password is wrong!!");
							System.out.println(
									"==============================================================================");

						}
						System.out.println("Do you want to continue?(Y/N)");
						status = br.readLine();

						if (status.equals("n") || status.equals("N")) {
							login = false;
						}
						break;

					case 5:
						login = false;
						break;

					default:
						System.out.println("Wrong Choice!!");
						break;

					}

				} while (login);
				
				System.out.println("==============================================================================");
				System.out.println("Bye. Have a nice day!!");
				System.out.println("==============================================================================");
				

			} else {
				System.out.println("Enter a valid input!!");
			}
		}
	}
}

									
			