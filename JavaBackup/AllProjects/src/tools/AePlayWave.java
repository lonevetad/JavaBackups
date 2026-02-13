package tools;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AePlayWave extends Thread {
	// new AePlayWave("test.wav").start();
	private String filename = "";

	private Position curPosition = Position.LEFT;

	private static final int EXTERNAL_BUFFER_SIZE = 1024 * 1024 * 16; // 16777216 byte
	// 128Kb = 524288

	public enum Position {
		LEFT, RIGHT, NORMAL
	}

	public AePlayWave() {
	}

	private AePlayWave(String wavfile) {
		super();
		filename = wavfile;
		setCurPosition(Position.NORMAL);
	}

	private AePlayWave(String wavfile, Position p) {
		super();
		filename = wavfile;
		setCurPosition(p);
	}

	public AePlayWave constructor_AePlayWave(String wavfile) {
		filename = wavfile;
		setCurPosition(Position.NORMAL);
		return new AePlayWave(filename);
	}

	public AePlayWave constructor_AePlayWave(String wavfile, Position p) {
		filename = wavfile;
		setCurPosition(p);
		return new AePlayWave(filename, getCurPosition()); // sto provando a
															// usare le classi
															// statiche (o
															// immutabili)
	}

	public void setFilename(String fileName) {
		filename = fileName;
	}

	public String getFilename() {
		return filename;
	}

	@Override
	public void run() {
		playThatFile();
	}

	public void playNewWaveFile(String wavfile) {
		filename = wavfile;
		setCurPosition(Position.NORMAL);
		playThatFile();
	}

	@SuppressWarnings("deprecation")
	public void sleepReproduction() // long ll)
	{
		/*
		 * try{ this.sleep(l); } catch ( Exception e) { e.printStackTrace(); }
		 */
		this.suspend();
		/*
		 * if ( l > 0) { this.setIsPlaying(true); this.setHowMuchMillisecToSleep(l); }
		 */
	}

	/*
	 * public void sleepReproduction() { try { this.stop(); } catch (Exception e) {
	 * e.printStackTrace(); } setIsPlaying(false); }
	 */

	@SuppressWarnings("deprecation")
	public void continueReproduction() { // don't need to set isPlaying to true
											// there, it will be in the
											// following method:
		setHowMuchMillisecToSleep(0);

		this.resume();
	}

	private boolean isPlaying = true;

	public boolean getIsPlaying() {
		return this.isPlaying;
	}

	public void setIsPlaying(boolean b) {
		isPlaying = b;
		if (b) {
			this.howMuchMillisecToSleep = (0);
		}
	}

	private long howMuchMillisecToSleep = 0;

	public long getHowMuchMillisecToSleep() {
		return this.howMuchMillisecToSleep;
	}

	public void setHowMuchMillisecToSleep(long l) {
		this.howMuchMillisecToSleep = l;
		if (l >= 0) {
			this.isPlaying = true;
		}
	}

	public void playThatFile() {

		File soundFile = new File(filename);
		if (!soundFile.exists()) {
			System.err.println("Wave file not found:\n" + filename);
			return;
		}

		// for ( int iii = 0; iii < 1; iii++) { // from there you can loop the
		// reproduction
		// for ( int iii = 0; iii < 1; iii++) { // from there you can loop the
		// reproduction
		boolean canContinue = false;
		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
			canContinue = true;
		} catch (UnsupportedAudioFileException uafe) {
			uafe.printStackTrace();
			System.out.println("File audio non supportato :\n" + getFilename());
			canContinue = false;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			canContinue = false;
		} catch (Exception eee) {
			eee.printStackTrace();
			canContinue = false;
		}
		if (canContinue) {
			// re-use the boolean variable "canContinue" to avoid the return
			// statement
			canContinue = false;

			AudioFormat format = audioInputStream.getFormat();
			SourceDataLine auline = null;

			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

			try {
				auline = (SourceDataLine) AudioSystem.getLine(info);
				auline.open(format);
				canContinue = true;
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (canContinue) {
				if (auline.isControlSupported(FloatControl.Type.PAN)) {
					FloatControl pan = (FloatControl) auline.getControl(FloatControl.Type.PAN);
					if (getCurPosition() == Position.RIGHT)
						pan.setValue(1.0f);
					else if (getCurPosition() == Position.LEFT)
						pan.setValue(-1.0f);
				}

				auline.start();
				int nBytesRead = 0;
				byte[] abData = new byte[getEXTERNAL_BUFFER_SIZE()];

				try {
					while (nBytesRead != -1) {

						// lo faccio dormire per farlo stoppare in modo poco
						// elegante e performante ma pratico e funzionale
						while ((this.getHowMuchMillisecToSleep() > 0) || !this.getIsPlaying()) {
							try {
								Thread.sleep(1);
								if (this.getIsPlaying()) {
									this.setHowMuchMillisecToSleep(this.getHowMuchMillisecToSleep() - 1);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						nBytesRead = audioInputStream.read(abData, 0, abData.length);
						if (nBytesRead >= 0)
							auline.write(abData, 0, nBytesRead);
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
					return;
				} catch (Exception e) {
					e.printStackTrace();
					// return;
				} finally {
					auline.drain();
					auline.close();
				}
			}
		}
		try {
			this.finalize();
		} catch (Throwable te) {
			te.printStackTrace();
		}
	}

	public int getEXTERNAL_BUFFER_SIZE() {
		return EXTERNAL_BUFFER_SIZE;
	}

	public Position getCurPosition() {
		return curPosition;
	}

	public void setCurPosition(Position curPosition) {
		this.curPosition = curPosition;
	}

}