package nlp.grader.objects;

public class Points {
	public Integer point1a = 0, point1b = 0, point1c = 0;
	public Integer point1d = 0, point2a = 0, point2b = 0, point3 = 0;
	
	public Points() {
		point1a = 0;
		point1b = 0;
		point1c = 0;
		point1d = 0;
		point2a = 0;
		point2b = 0;
		point3 = 0;
	}
	
	public enum Weightage {
		P1A(1), P1B(1), P1C(1), P1D(2),
		P2A(1), P2B(3), P3(1);
		
		private final int weight;
		
		Weightage(int weight) {
			this.weight = weight;
		}
		
		public int weight() {
			return this.weight;
		}
	}
}
