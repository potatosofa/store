package net.paraoffice.framework;

import java.io.BufferedReader;
import java.io.IOException;

public interface RequestData {

	void load(BufferedReader br) throws IOException;

	int getID();

}
