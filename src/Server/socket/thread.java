package Server.socket;

import java.io.*;
import java.net.Socket;
import java.util.*;

import Object.Code;
import Object.CodeResult;

public class thread implements Runnable {
	private Socket socket = null;
	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;
	private encryption encryption;
	private LogFileHelper logFileHelper; 

	public thread(Socket socket) {
		try {
			this.socket = socket;
			this.out = new ObjectOutputStream(this.socket.getOutputStream());
			this.in = new ObjectInputStream(this.socket.getInputStream());
			generateKey();
			writeToLog("[Notification] Client: " + this.socket + " is connected");
		} catch(java.net.SocketException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		handleData();
	}

	public void close() {
		try {
			if (socket != null) {
				this.in.close();
				this.out.close();
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public void send(CodeResult codeResult) {
		try {
			byte[] bytes = encryption.encryptData(codeResult);
			this.out.writeObject(bytes);
			this.out.flush();
		} catch (java.net.SocketException | java.lang.NullPointerException | java.io.EOFException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Object receive() {
		try {
			byte[] bytes = (byte[]) this.in.readObject();
			Object object = (Object) encryption.decryptData(bytes);
			return object;
		} catch (java.net.SocketException | java.lang.NullPointerException | java.io.EOFException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void generateKey() {
		try {
			// encryption = new encryption();
			// PublicKey publicKey = (PublicKey) this.in.readObject();

			// encryption.setPublicKey(publicKey);
			// encryption.encryptKey();

			// this.out.writeObject(encryption.getEncryptedKey());
			// this.out.flush();

			encryption = new encryption();
			this.out.writeObject(encryption.getKeyPair().getPublic());
			this.out.flush();

			byte[] encryptedKey = (byte[]) this.in.readObject();
			encryption.setEncryptedKey(encryptedKey);
			encryption.decryptKey();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleData() {
		while (this.socket != null) {
			try {
				Object object = this.receive();
				if (object != null) {
					Code code = (Code) object;
					execute(code);
				} else {
					writeToLog("[Notification] Client: " + this.socket + " lost connection");
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}

	private void execute(Code code) {
		CodeResult codeResult = new CodeResult();
		try {
			Random rd = new Random();
			int index = rd.nextInt(100000) + 900000;
			String filePath = ".\\src\\Server\\temp\\" + index;
			String lang = code.getLanguage() , src = code.getSource(), input = code.getInput();
			String command = "", lines = "";
			ProcessBuilder pb = new ProcessBuilder();
			Process process = null;
			ProcessHandle ph = null;
			PrintWriter pw = null;
			BufferedReader br = null;

			switch (lang) {
				case "C":
					String processName = index + ".exe";
					String fileExe = filePath + ".exe";
					filePath += ".c";
					writeToFile(filePath, src);
					command = "gcc " + filePath + " -o " + fileExe;
					pb.command("cmd.exe", "/C", command + " 2>&1");
					process = pb.start();
					int exitedCode = process.waitFor();
					if (exitedCode == 0) {
						pb.command("cmd.exe", "/c", fileExe + " 2>&1");
						pb.redirectErrorStream(true);
						process = pb.start();
						stopProcess("taskkill /IM " + processName + " /F");
					} 
					break;
				case "Python":
					command = ".\\cf\\Compiler\\Python311\\python.exe";
					filePath += ".py";
					writeToFile(filePath, src);
					pb.command("cmd.exe", "/c", command + " " + filePath + " 2>&1");
					process = pb.start();
					pb.redirectErrorStream(true);
					ph = process.toHandle();
					stopProcess("taskkill /IM " + ph.pid() + " /F /T");
					break;
				case "Javascript":
					command = "node";
					filePath += ".js";
					writeToFile(filePath, src);
					pb.command("cmd.exe", "/c", command + " " + filePath + " 2>&1");
					process = pb.start();
					pb.redirectErrorStream(true);
					ph = process.toHandle();
					stopProcess("taskkill /IM " + ph.pid() + " /F /T");
					break;
				case "Java":
					filePath += ".java";
					writeToFile(filePath, src);
					// command = "javac";
					// pb.command("cmd.exe", "/c", command + " " + filePath + " 2>&1");
					// process = pb.start();
					// exitedCode = process.waitFor();
					// if (exitedCode == 0) {
					command = "java";
					pb.command("cmd.exe", "/c", command + " " + filePath + " 2>&1");
					process = pb.start();
					pb.redirectErrorStream(true);
					ph = process.toHandle();
					stopProcess("taskkill /IM " + ph.pid() + " /F /T");
					// } 
					break;
				default:
					break;
			}
			
			pw = new PrintWriter(process.getOutputStream());
			br = new BufferedReader(new InputStreamReader(process.getInputStream()));

			pw.println(input);
			pw.flush();
			pw.close();
			
			String line = "";
			while ((line = br.readLine()) != null) {
				lines += line + "\n";
			}
			
			int exitedCode = process.waitFor();
			lines += "Exited code: " + exitedCode;
			if (exitedCode == 0) codeResult.setFormattedSrc(format(code, index));
			else codeResult.setFormattedSrc(src);

			if (lines.length() > 1000000)
				codeResult.setExecResult("Exited code: " + exitedCode);
			else	
				codeResult.setExecResult(lines);

			send(codeResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void stopProcess(String fileExe){
		StopProcess sp = new StopProcess(fileExe);
		Thread thread = new Thread(sp);
		
		Timer timer = new Timer();
		TimeOutTask timeOutTask = new TimeOutTask(thread, timer);
		timer.schedule(timeOutTask, 2000);
	}

	private String format(Code code, int index) {
		try {
			Random rd = new Random();
			String filePath = ".\\src\\Server\\temp\\" + index;
			ProcessBuilder pb = new ProcessBuilder();
			String lang = code.getLanguage(), src = code.getSource();
			String command = "", result = "";

			switch (lang) {
				case "C":
					filePath += ".c";
					writeToFile(filePath, src);
					command = ".\\cf\\Formatter\\astyle-3.4.10-x64\\astyle.exe";
					pb.command("cmd.exe", "/c", command + " -H -U -xe -p -f -E -n -A1 " + filePath);
					pb.start();
					break;
				case "Python":
					command = "black";
					filePath += ".py";
					writeToFile(filePath, src);
					pb.command("cmd.exe", "/c", command + " " + filePath);
					pb.start();
					break;
				case "PHP":
					command = "";
					filePath += ".php";
					writeToFile(filePath, src);
					pb.command("cmd.exe", "/c", command + " " + filePath + " > " + filePath);
					pb.start();
					break;
				case "Javascript":
					command = ".\\cf\\Formatter\\astyle-3.4.10-x64\\astyle.exe";
					filePath += ".js";
					writeToFile(filePath, src);
					pb.command("cmd.exe", "/c", command + " -H -U -xe -p -f -E -n -A1 " + filePath);
					pb.start();
					break;
				case "Java":
					command = ".\\cf\\Formatter\\astyle-3.4.10-x64\\astyle.exe";
					filePath += ".java";
					writeToFile(filePath, src);
					pb.command("cmd.exe", "/c", command + " -H -U -xe -p -f -E -n " + filePath);
					pb.start();
					break;
			}

			Thread.sleep(1000);
			result = readFromFile(filePath);
			return result;

		} catch (Exception e) {
			e.printStackTrace();
			return ""; 
		}
	}

	private void writeToFile(String filePath, String content) {
		try {
			File file = new File(filePath);
			file.createNewFile();
			FileWriter fileWriter = new FileWriter(filePath);
			fileWriter.write(content);
			fileWriter.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String readFromFile(String filePath) {
		String lines = "";
		try {
			BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			String line = "";
			while ((line = fileReader.readLine()) != null) {
				lines += line + "\n";
			}
			return lines;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lines;
	}

	private void writeToLog(String line) {
		try {
			logFileHelper = new LogFileHelper(new File(".\\src\\Server\\socket\\log.txt"));
			logFileHelper.tryWriteLine("[Notification] Client: " + this.socket + " connected");
			logFileHelper.tryClose();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
