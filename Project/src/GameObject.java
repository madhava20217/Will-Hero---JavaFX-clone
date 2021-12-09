import javafx.geometry.Bounds;
import javafx.scene.Node;

import java.io.Serializable;


// TODO: make objects be able to return from out of bounds
// TODO: try harder to make hero [and other bodies] match original movement pattern
public class GameObject implements Serializable {
	private final Node model;
	private final float mass;
	private final float[] pos;
	private final float[] vel;
	private final float[] acc;
	private boolean rendered;
	private final boolean gravity_affected;
	
	public float get_acc (int axis) {
		return acc[axis];
	}
	
	public Node getModel () {
		return model;
	}

	private boolean is_out_of_bounds(){
		Bounds b = model.getBoundsInParent();
		// TODO: make this proper before release
		return b.getMaxX()< 20 || b.getMinX() > 1000 || b.getMaxY() < 20 || b.getMinY() > 500;
		// return pos[0]+b.getWidth()/2 < 20 || pos[0]-b.getWidth()/2 > 1000 || pos[1]+b.getHeight()/2 < 20 || pos[1]-b
		// .getHeight()/2 > 500;
	}
	
	GameObject (Node _model, float[] v, float[] a, float m, boolean g){
		model = _model;
		pos = new float[]{(float)_model.getTranslateX(),(float)_model.getTranslateY()};
		vel = v;
		acc = a;
		mass = m;
		gravity_affected = g;
		rendered = true;
	}
	
	public void set_acc(int axis, float value){
		if (!(axis == 0 || axis == 1)) return;
		acc[axis] = value;
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
		}
		
		int dir = (int) Math.signum(vel_next);  // friction will oppose this direction
		acc[axis] -= dir*value;
	}
	
	public void move(){
		if(gravity_affected){
			apply_gravity();
		}
		for (int axis = 0; axis < 2; axis++) {  // move in both axes
			pos[axis] += vel[axis];
			vel[axis] += acc[axis];
		}
		if(rendered && this.is_out_of_bounds()){
			this.derender();
		}
		else if(!rendered && !this.is_out_of_bounds()) {
			this.render();
		}
	}
	
	public void refresh(){ // refreshes the posiiton on the screen
		model.setTranslateX(pos[0]);
		model.setTranslateY(pos[1]);
	}
	
	public void collide (GameObject other, float e){
		if (e < 0 || e > 1) return;
		
		// get axis of collision
		Bounds this_bnds = model.getBoundsInParent();
		Bounds other_bnds = other.model.getBoundsInParent();
		float this_max_x = (float) this_bnds.getMaxX();
		float this_min_x = (float) this_bnds.getMinX();
		float other_min_x = (float) other_bnds.getMinX();
		float other_max_x = (float) other_bnds.getMaxX();
		
		float this_max_y = (float) this_bnds.getMaxY();
		float this_min_y = (float) this_bnds.getMinY();
		float other_min_y = (float) other_bnds.getMinY();
		float other_max_y = (float) other_bnds.getMaxY();
		
		float x_overlap = Math.max(0, Math.min(this_max_x, other_max_x) - Math.max(this_min_x, other_min_x));
		float y_overlap = Math.max(0, Math.min(this_max_y, other_max_y) - Math.max(this_min_y, other_min_y));
		
		int axis = (x_overlap > y_overlap)? 1: 0;
		
		// don't do anything if they're moving away from each other already
		if ((this.vel[axis]*other.vel[axis]) < 0) {
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
		acc[1] = 0.08F;
	}
	
	public boolean touching(GameObject other){
		return model.getBoundsInParent().intersects(other.model.getBoundsInParent());
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
