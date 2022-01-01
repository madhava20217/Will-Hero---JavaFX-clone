import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.util.UUID;

public class GameObject implements Serializable{
	// INSTANCE VARIABLES
	private final UUID ID;
	private final float[] pos;  // current position
	private final float[] p0;  // starting position
	private final float[] vel;
	private final float[] acc;
	
	private transient ImageView model;  // cannot be serialised
	private final String imagePath;  // filepath of sprite
	private final float[] size;
	
	private final float mass;
	private boolean rendered;  // do i want to put it on the screen?
	private final boolean tangible;  // do i want it to bounce off of other things?
	private boolean gravity_affected;
	
	GameObject (float[] _pos, float[] v, float[] a, float m, boolean g, boolean t, String sprite, float[] _size) {
		this.ID = UUID.randomUUID();
		imagePath = sprite;
		size = _size;
		pos = _pos;
		p0 = _pos.clone();
		setModel();
		vel = v;
		acc = a;
		mass = m;
		gravity_affected = g;
		rendered = true;
		tangible = t;
	}
	
	// MOVEMENT RELATED GETTERS / SETTERS
	protected float[] getPos () {
		return pos;
	}
	
	protected float[] getVel () {
		return vel;
	}
	
	protected float[] getP0 () {
		return p0;
	}
	
	public float get_vel (int axis) {
		assert (axis == 0 || axis == 1);
		return vel[axis];
	}
	
	protected void setPos (float x, float y) {
		this.pos[0] = x;
		this.pos[1] = y;
	}
	
	public void set_vel (int axis, float value) {
		assert (axis == 0 || axis == 1);
		vel[axis] = value;
	}
	
	public void apply_force (int axis, float value) {
		assert (axis == 0 || axis == 1);
		acc[axis] += value / mass;
	}
	
	// MISCELLANEOUS GETTERS / SETTERS
	protected void setG (boolean g) {
		gravity_affected = g;
	}
	
	public Node getModel () {
		return model;
	}
	
	public boolean isTangible () {
		return tangible;
	}
	
	public float[] getSize () {
		return size;
	}
	
	public boolean isRendered () {
		return rendered;
	}
	
	public UUID getID () {
		return ID;
	}
	
	public void setModel () { // only sets sprite based on instance variables
		model = new ImageView();
		if (imagePath != null)
			model.setImage(new Image(imagePath));
		model.setLayoutX(p0[0]);
		model.setLayoutY(p0[1]);
		float panCam = GameController.getPanCam();
		model.setTranslateX(pos[0] - p0[0] - panCam);
		model.setTranslateY(pos[1] - p0[1]);
		model.setFitWidth(size[0]);
		model.setFitHeight(size[1]);
	}
	
	// MOVEMENT BASED METHODS
	public void move () {
		// called every frame
		if (gravity_affected) {
			apply_gravity();
		}
		for (int axis = 0; axis < 2; axis++) {  // move in both axes
			pos[axis] += vel[axis];
			vel[axis] += acc[axis];
			acc[axis] = 0; // force has to be applied to be each frame to change things
		}
		if (rendered && this.is_out_of_bounds()) {
			this.derender();
		} else if (!rendered && !this.is_out_of_bounds()) {
			this.render();
		}
	}
	
	public void bounce (GameObject other, float e) {
		if (e < 0 || e > 1) return;
		float[] overlaps = this.getOverlaps(other);
		float x_overlap = overlaps[0];
		float y_overlap = overlaps[1];
		
		// get axis of collision
		int axis = (x_overlap > y_overlap) ? 1 : 0;
		
		// don't do anything if they're moving away from each other already
		if ((other.vel[axis] - this.vel[axis]) * (other.pos[axis] - this.pos[axis]) > 0) {
			return; // don't do anything if they're moving away from each other already
		}
		
		// check for large mass difference
		float m1 = (this.mass / other.mass > 25) ? 10000000 : this.mass;
		float m2 = (other.mass / this.mass > 25) ? 10000000 : other.mass;
		
		// set velocity in axis of collision
		float u1 = this.get_vel(axis);
		float u2 = other.get_vel(axis);
		
		this.set_vel(axis, (m1 * u1 + m2 * u2 - m2 * e * (u1 - u2)) / (m1 + m2));
		other.set_vel(axis, (m1 * u1 + m2 * u2 - m1 * e * (u2 - u1)) / (m1 + m2));
	}
	
	public void apply_gravity () {
		apply_force(1, 0.08F * mass);
	}
	
	public void decelerate (int axis, float value) {
		// exists for forces like string tension and friction that self-adjust
		assert (axis == 0 || axis == 1) && value > 0;
		
		float vel_next = vel[axis] + acc[axis]; // velocity on the next frame
		
		if (Math.abs(vel_next) < value) {  // if friction can make object still
			acc[axis] = -vel[axis];  // it will make it still
			return;
		}
		
		int dir = (int)Math.signum(vel_next);  // friction will oppose this direction
		apply_force(axis, -dir * value);
	}
	
	// UTILITY METHODS FOR MOVEMENT
	protected boolean is_out_of_bounds () {
		Bounds b = model.getBoundsInParent();
		float pc = GameController.getPanCam();
		// {pos - pc} is visual [on the screen], absolute position [not relative to starting position]
		// there is no pc in y because we are not doing panning in y axis
		return pos[0] - pc > 1040 || pos[0] - pc + b.getWidth() < 0 || pos[1] + b.getHeight() < 0 || pos[1] > 580;
	}
	
	public void refresh () { // refreshes the position on the screen
		float panCam = GameController.getPanCam();
		model.setTranslateX(pos[0] - p0[0] - panCam);
		model.setTranslateY(pos[1] - p0[1]);
	}
	
	public float[] getOverlaps (GameObject other) {
		// returns the overlaps with another object
		Bounds this_bnds = this.model.getBoundsInParent();
		Bounds other_bnds = other.model.getBoundsInParent();
		float this_min_x = this.pos[0];
		float this_max_x = (float)(this.pos[0] + this_bnds.getWidth());
		float other_min_x = other.pos[0];
		float other_max_x = (float)(other.pos[0] + other_bnds.getWidth());
		
		float this_min_y = this.pos[1];
		float this_max_y = (float)(this.pos[1] + this_bnds.getHeight());
		float other_min_y = other.pos[1];
		float other_max_y = (float)(other.pos[1] + other_bnds.getHeight());
		
		float x_overlap = Math.max(0, Math.min(this_max_x, other_max_x) - Math.max(this_min_x, other_min_x));
		float y_overlap = Math.max(0, Math.min(this_max_y, other_max_y) - Math.max(this_min_y, other_min_y));
		
		return new float[]{x_overlap, y_overlap};
	}
	
	// TOGGGLING RENDERING / SIMULATION
	public void render () {
		model.setVisible(true);
		rendered = true;
	}
	
	public void derender () {
		model.setVisible(false);
		rendered = false;
		if (pos[1] > 580) { // fell down off-screen
			// nothing comes back once it falls off-screen, except the Hero.
			if (!(this instanceof Hero) && GameController.getGameInstance() != null) {
				GameController.getGameInstance().deregister(this);
			}
		}
	}
	
	public void remove () {
		derender();
		if (GameController.getGameInstance() != null) {
			// it may happen that the game tries to remove an object just after it has ended.
			GameController.getGameInstance().deregister(this);
		}
	}
}
