public final class Platform extends GameObject{
	private static String getfile (int cat) {
		// Facade Design pattern
		assert (0 <= cat && cat <= 2);
		return "images/platform_" + (cat + 1) + ".png";
	}
	
	private static float[] getsize (int cat) {
		assert (0 <= cat && cat <= 2);
		float[][] sizes = {{352, 156}, {387, 197}, {387, 197}};
		return sizes[cat];
	}
	
	Platform (float[] pos, int cat) {
		super(pos, new float[]{0, 0}, new float[]{0, 0}, 1000, false, true, getfile(cat), getsize(cat));
	}
	
	Platform (float[] pos, String sprite, float[] size) {
		super(pos, new float[]{0, 0}, new float[]{0, 0}, 1000, false, true, sprite, size);
	}
}
