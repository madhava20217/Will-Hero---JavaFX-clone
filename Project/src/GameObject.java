import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;

import java.io.Serializable;

public class GameObject implements Serializable {
	private Shape model;
	private float mass;
	private float[] pos;
	private float[] vel;
	private float[] acc;
	private boolean rendered;
	private boolean gravity_affected;
	
	public float get_acc (int axis) {
		return acc[axis];
	}
	
	public Shape getModel () {
		return model;
	}
	
	private float[] get_coords(Shape s){
		Point2D c = s.localToScene(s.getLayoutX()+s.getTranslateX(),s.getLayoutY()+s.getTranslateY());
		return new float[]{(float)c.getX(), (float)c.getY()};
	}
	
	GameObject (Shape _model, float[] v, float[] a, float m, boolean g){
		model = _model;
		pos = get_coords(_model);
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
		for (int axis = 0; axis < 2; axis++) {  // move in both axes
			pos[axis] += vel[axis];
			vel[axis] += acc[axis];
			if(this.isRendered() && (pos[axis] < -100 || pos[axis] > 1200)){
				this.derender();
			}
		}
		if(this.isRendered()) return;
		for(int axis = 0; axis < 2; axis++){
			if(pos[axis] < -100 || pos[axis] > 1200){
				return;
			}
		}
		this.render();
	}
	
	public void refresh(){ // refreshes the posiiton on the screen
		Point2D c = model.sceneToLocal(pos[0], pos[1]);
		model.setTranslateX(c.getX());
		model.setTranslateY(c.getY());
	}
	
	public void collide (GameObject other, float e){
		if (e < 0 || e > 1) return;
		
		// get axis of collision
		float this_max = (float) this.getModel().getBoundsInParent().getMaxX() - vel[0];
		float this_min = (float) this.getModel().getBoundsInParent().getMinX() - vel[0];
		float other_min = (float) other.getModel().getBoundsInParent().getMinX() - other.vel[0];
		float other_max = (float) other.getModel().getBoundsInParent().getMaxX() - other.vel[0];
		
		int axis = (this_max < other_min || this_min > other_max)?0:1;
		
		if((this.pos[axis]-other.pos[axis])*(this.vel[axis]-other.vel[axis]) < 0){
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
		acc[1] = 0.05F;
	}
	
	public boolean touching(GameObject other){
		return this.getModel().getBoundsInParent().intersects(other.getModel().getBoundsInParent());
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
