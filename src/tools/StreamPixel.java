package tools;

import java.io.Serializable;

public interface StreamPixel extends Serializable {
	public void forEach(ConsumerPixel consumer);
}