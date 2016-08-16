package com.foosball.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.foosball.game.utils.Constants;

public class FoosballGame extends ApplicationAdapter {
	Texture img;

	public static final String TITLE = "Tutorial";
	public static final int V_WIDTH = 720;
	public static final int V_HEIGHT = 480;
	public static final float SCALE = 2f;

	private OrthographicCamera camera;
	private SpriteBatch batch;


	private OrthogonalTiledMapRenderer Otmr;
	private TiledMap tMap;

	private Box2DDebugRenderer debugRenderer;
	private World world;
	private Body player, platform;

	public enum ShapeType {
		BOX(1),
		CIRCLE(2);

		private int value;

		private ShapeType(int val){
			value = val;
		}

		public int getValue(){
			return value;
		}
	}

	
	@Override
	public void create () {

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w / SCALE, h / SCALE);

		world = new World(new Vector2(0,-9.8f), false);
		debugRenderer = new Box2DDebugRenderer();

		player = createBox(8, 10, 32, 32, false, ShapeType.CIRCLE);
		platform = createBox(4, 0, 256, 96, true, ShapeType.BOX);

//		batch = new SpriteBatch();
//		batch.setProjectionMatrix(camera.combined);


//		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

	}

	@Override
	public void render () {
		update(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		debugRenderer.render(world,camera.combined.scl(Constants.PPM));
//		batch.begin();
//		//batch.draw(img, 0, 0);
//		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false,width/2,height/2);
		//gsm.resize((int) (720 / SCALE), (int) (480 / SCALE));
	}

	private void update(float deltaTime) {
		world.step(1/60f,6,2);

		cameraUpdate(deltaTime);
	}

	private void cameraUpdate(float deltaTime) {
		Vector3 position = camera.position;
		position.x = player.getPosition().x * Constants.PPM;
		position.y = player.getPosition().y * Constants.PPM;
		camera.position.set(position);

		camera.update();

	}

	@Override
	public void dispose() {
		//gsm.dispose();
		batch.dispose();
		world.dispose();
		debugRenderer.dispose();
	}

	private Body createBox(int x, int y, int width, int height, boolean isStatic, ShapeType type ) {
		Body pBody;
		BodyDef def = new BodyDef();

		if (isStatic)
			def.type = BodyDef.BodyType.StaticBody;
		else
			def.type = BodyDef.BodyType.DynamicBody;

		def.position.set(x/Constants.PPM, y/Constants.PPM);
		def.fixedRotation = true;
		pBody = world.createBody(def);

		switch (type) {
			case CIRCLE:
				CircleShape shape = new CircleShape();
				shape.setRadius(width/Constants.PPM);
				pBody.createFixture(shape,1.0f);
				shape.dispose();

				break;
			case BOX:
				PolygonShape pShape = new PolygonShape();
				pShape.setAsBox(width/2/Constants.PPM, height/2/Constants.PPM);
				pBody.createFixture(pShape,1.0f);
				pShape.dispose();
				break;

		}

		//shape.setPosition(new Vector2());



		return pBody;
	}
	public OrthographicCamera getCamera() {
		return camera;
	}
	public SpriteBatch getBatch() {
		return batch;
	}
}
