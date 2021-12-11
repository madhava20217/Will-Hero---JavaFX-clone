import javafx.geometry.Bounds;
import javafx.scene.Node;

import java.io.Serializable;

public class GameObject implements Serializable {
	private final Node model;
	private final float mass;
	private final float[] pos;
	private final float[] p0;
	private final float[] vel;
	private final float[] acc;
	private boolean rendered;
	private final boolean gravity_affected;
	private final boolean tangible;
	
	protected float[] getPos(){
		return pos;
	}
	protected float[] getVel(){
		return vel;
	}
	
	public float get_acc (int axis) {
		return acc[axis];
	}
	
	public Node getModel () {
		return model;
	}
	
	public boolean isTangible(){
		return tangible;
	}

	private boolean is_out_of_bounds(){
		Bounds b = model.getBoundsInParent();
		float pc = GameController.getPanCam();
		return pos[0] - pc > 1020 || pos[0] - pc + b.getWidth() < 20 || pos[1] + b.getHeight() < 0 || pos[1] > 500;
	}
	
	GameObject (Node _model, float[] v, float[] a, float m, boolean g, boolean t){
		model = _model;
		p0 = new float[]{(float)(_model.getLayoutX()),(float)(_model.getLayoutY())};
		pos = p0.clone();
		vel = v;
		acc = a;
		mass = m;
		gravity_affected = g;
		rendered = true;
		tangible = t;
	}
	
	public void apply_force(int axis, float value){
		if (!(axis == 0 || axis == 1)) return;
		acc[axis] += value/mass;
	}
	
	public void set_vel(int axis, float value){
		if (!(axis == 0 || axis == 1)) return;
		vel[axis] = value;
	}
	
	public float get_vel(int axis){
		return vel[axis];
	}
	
	// exists for forces like string tension and friction that self-adjust
	public void decelerate(int axis, float value){
		if (!((axis == 0 || axis == 1) && value > 0 )) return;
		
		float vel_next = vel[axis]+acc[axis]; // velocity on the next frame
		
		if (Math.abs(vel_next) < value){  // if friction can make object still
			acc[axis] = -vel[axis];  // it will make it still
			return;
		}
		
		int dir = (int) Math.signum(vel_next);  // friction will oppose this direction
		apply_force(axis,-dir*value);
	}
	
	public void move(){
		if(gravity_affected){
			apply_gravity();
		}
		for (int axis = 0; axis < 2; axis++) {  // move in both axes
			pos[axis] += vel[axis];
			vel[axis] += acc[axis];
			acc[axis] = 0;
		}
		if(rendered && this.is_out_of_bounds()){
			this.derender();
		}
		else if(!rendered && !this.is_out_of_bounds()) {
			this.render();
		}
	}
	
	public void refresh(){ // refreshes the position on the screen
		float panCam = GameController.getPanCam();
		model.setTranslateX(pos[0]-p0[0]-panCam);
		model.setTranslateY(pos[1]-p0[1]);
	}
	
	public void bounce (GameObject other, float e, float x_overlap, float y_overlap){
		if (e < 0 || e > 1) return;
		
		// get axis of collision
		int axis = (x_overlap > y_overlap)? 1: 0;
		
		// don't do anything if they're moving away from each other already
		if ((other.vel[axis] - this.vel[axis])*(other.pos[axis]-this.pos[axis]) > 0) {
			return; // don't do anything if they're moving away from each other already
		}
		
		// check for "infinite mass"
		float m1 = (this.mass / other.mass > 25)? 10000000: this.mass;
		float m2 = (other.mass / this.mass > 25)? 10000000: other.mass;
		
		// set velocity in axis of collision
		float u1 = this.get_vel(axis);
		float u2 = other.get_vel(axis);
		
		float a1 = this.get_acc(axis);
		float a2 = other.get_acc(axis);
		
		this.set_vel(axis, (m1*u1 + m2*u2 -m2*e*(u1-u2))/(m1+m2));
		other.set_vel(axis, (m1*u1 + m2*u2 -m1*e*(u2-u1))/(m1+m2));
	}
	
	public void apply_gravity(){
		apply_force(1,0.08F*mass);
	}
	
	public float[] getOverlaps(GameObject other){
		// returns the overlaps with another object
		Bounds this_bnds = this.model.getBoundsInParent();
		Bounds other_bnds = other.model.getBoundsInParent();
		float this_min_x = this.pos[0];
		float this_max_x = (float) (this.pos[0]+this_bnds.getWidth());
		float other_min_x = other.pos[0];
		float other_max_x = (float) (other.pos[0]+other_bnds.getWidth());
		
		float this_min_y = this.pos[1];
		float this_max_y = (float) (this.pos[1]+this_bnds.getHeight());
		float other_min_y = other.pos[1];
		float other_max_y = (float) (other.pos[1]+other_bnds.getHeight());
		
		float x_overlap = Math.max(0, Math.min(this_max_x, other_max_x) - Math.max(this_min_x, other_min_x));
		float y_overlap = Math.max(0, Math.min(this_max_y, other_max_y) - Math.max(this_min_y, other_min_y));
		
		return new float[]{x_overlap, y_overlap};
	}

  public void render(){
		rendered = true;
  }

  public void derender(){
		rendered = false;
  }
	
	public boolean isRendered(){
		return rendered;
	}
}
