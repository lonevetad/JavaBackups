package tests;

@Deprecated
public class TestMatrix {

	public TestMatrix() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return false if the matrix is null or void or the dimensions are incorrect.
	 *         If the given area, from P(<code>xStart</code>, <code>yStart</code>)
	 *         and R(<code>xStart + width</code>, <code>yStart + height</code>)
	 *         cannot be fully contained into the matrix or this contains null raws
	 *         , false is still returned BUT the action is performed in a safer but
	 *         slower mode.
	 */
	public static boolean fillRect_Concentrico(int[][] m, int argb, int xStart, int yStart, int width, int height) {
		boolean ret = m != null && m.length > 0 && xStart >= 0 && yStart >= 0 && width > 0 && height > 0;
		if (ret) {
			// controllo che la sottoarea stia nella matrice E non ci siano null
			boolean containNulls = false, notFullyInside = m.length < (yStart + height);
			int i = 0, r, c, row[], row2[] = null, yy = yStart, xx = xStart + width, j = 0,
					// -2 e non -1 per evitare di ricalcare lo stesso pixel
					reducingWidth = xStart + (width - 1), //
					reducingHeight = yStart + (height - 1);
			// System.out.println("notFullyInside : " + notFullyInside);
			if (notFullyInside) {
				while (/* (!notFullyInside) && */ (i++ < height) && (yy < m.length)) {
					row = m[yy++];
					containNulls |= row == null;
					if (row != null) {
						notFullyInside |= row.length > xx;
					}
				}
			}
			// THE CORE
			// System.out.println("notFullyInside : " + notFullyInside + " ___
			// \t containNulls : " + containNulls);
			if (notFullyInside) {
				if (containNulls) {
					// il caso peggiore : ci sono null e devo fare check sulle
					// coordinate
					System.out.println("NON ci sto dentro E HO RIGHE NULLE ! D:");
					i = Math.min(width, height);
					i = (i >>> 1) + ((i << 31) >>> 31);
					while (j <= i) {
						r = /* yy = */ yStart + j;
						if (r < m.length) {
							row = m[r];
							// row2 = m[reducingHeight];
							// ciclo verticale
							c = xStart + j;
							while (r <= reducingHeight) {
								if (r < m.length) {
									row2 = m[r];
									if (row2 != null) {
										if (c < row2.length) {
											row2[c] = argb;
										}
										if (reducingWidth < row2.length) {
											row2[reducingWidth] = argb;
										}
									}
								}
								r++;
							}
							// alla fine avro "row2 = m[reducingHeight];"
							// ciclo orizzontale
							c++;
							if (row != null || row2 != null) {
								while (c <= reducingWidth) {
									if (row != null && c < row.length) {
										row[c] = argb;
									}
									if (row2 != null && c < row2.length) {
										row2[c] = argb;
									}
									c++;
								}
							}
						}
						reducingWidth--;
						reducingHeight--;
						j++;
					}
				} else {
					// null-safe
					System.out.println("NON ci sto dentro ma non ho righe nulle");
					i = Math.min(width, height);
					i = (i >>> 1) + ((i << 31) >>> 31);
					while (j <= i) {
						r = /* yy = */ yStart + j;
						if (r < m.length) {
							row = m[r];
							// row2 = m[reducingHeight];
							// ciclo verticale
							c = xStart + j;
							while (r <= reducingHeight) {
								if (r < m.length) {
									row2 = m[r];
									if (c < row2.length) {
										row2[c] = argb;
									}
									if (reducingWidth < row2.length) {
										row2[reducingWidth] = argb;
									}
								}
								r++;
							}
							// alla fine avro "row2 = m[reducingHeight];"
							// ciclo orizzontale
							c++;
							while (c <= reducingWidth) {
								if (c < row.length) {
									row[c] = argb;
								}
								if (c < row2.length) {
									row2[c] = argb;
								}
								c++;
							}
						}
						reducingWidth--;
						reducingHeight--;
						j++;
					}
				}
			} else {
				if (containNulls) {
					System.out.println("tutto dentro ma qualche riga è null");
					i = Math.min(width, height);
					i = (i >>> 1) + ((i << 31) >>> 31);
					while (j <= i) {
						row = m[r = /* yy = */ yStart + j];
						// ciclo verticale
						c = xStart + j;
						while (r <= reducingHeight) {
							row2 = m[r++];
							if (row2 != null) {
								row2[c] = row2[reducingWidth] = argb;
							}
						}
						// alla fine avro "row2 = m[reducingHeight];"
						// ciclo orizzontale
						c++;
						if (row != null || row2 != null) {
							while (c <= reducingWidth) {
								if (row != null) {
									row[c] = argb;
								}
								if (row2 != null) {
									row[c] = row2[c] = argb;
								}
								c++;
							}
						}
						reducingWidth--;
						reducingHeight--;
						j++;
					}

				} else {
					// il caso migliore !!!
					i = Math.min(width, height);
					i = (i >>> 1) + ((i << 31) >>> 31);
					while (j <= i) {
						row = m[r = /* yy = */ yStart + j];
						// row2 = m[reducingHeight];
						// ciclo verticale
						c = xStart + j;
						while (r <= reducingHeight) {
							row2 = m[r++];
							row2[c] = row2[reducingWidth] = argb;
						}
						// alla fine avro "row2 = m[reducingHeight];"
						// ciclo orizzontale
						c++;
						while (c <= reducingWidth) {
							row[c] = row2[c] = argb;
							c++;
						}
						reducingWidth--;
						reducingHeight--;
						j++;
					}
					return true; // caso unico
				}
			}
			ret = false;
		}
		return ret;
	}

}
