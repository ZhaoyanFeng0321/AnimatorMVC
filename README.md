# Animator with MVC design pattern

Demo: https://www.youtube.com/watch?v=pc6OHgaVnBc

### **Model**

---------------------------
**I. Description:**
- The model store and manage data of a 2D Animation, which supports a series of animation operations on various 2D shapes.
- It stores the data of each shape, and a series of motions it performing in the animator and allow access shape
and animation data simply by the identifier of the shape.
- It allows a shape to do various transformations (e.g. move, scale, change color, etc.)
and do several changes in attributes at the same time.


**II. Features to highlight:**
1. Flexibility:
    - add more motion types by implementing `<Action>`
    - add more shape types by implementing `<IMutableShape2D>`
2. Data Security:
    - `IShape2D` and `IReadableModel` are read-only, only supports getter methods.
    Secure data while the controller and view accessing animation data.
    - All getters return copy of data, in case accidentally modified by others.
3. User Friendly:
    - Easy to add transformation: just input identifier, time, before and after shape state,
    the model will classify which transformation the shape make and whether it is valid and legal to perform.
    - No need to input data in exactly time order, it can sort the data with the assist of TimePeriod,
    which implements `Comparable` and override compareTo method to redefine a sorter.


**III. How it works?**
1. Why manage shapes data with `LinkedHashMap<String, IAnimateShape>`?
    - The LinkedHashMap store data in the input order.
    - String: identifier of a shape, as key in a map. Allow quick access data and make change and quick check if shape contain in this animation.
    - `IAnimateShape` take advantage of delegation, it handles animation detail of a specific shape,
    including the initial and final state of shape and list of `Action`.

2. How to handle transformation data?
    - `Action` provides method to validate whether a series of animation of a shape with `isConsistent()`
    and `isSynchronous()` method, associate with `TimePeriod` to check if action is legal to act at this time period.


_***IV. Refactor model in A7: What changed and why?**_
1. Structure: Separate interface to readable and mutable.
- `<IReadableModel>`:\
    only getter methods, allow access data but not mutate data. Usually used by View.
- `<IAnimatorModel>` extends `<IReadableModel>`:\
    contains setter/mutate methods, allow mutate data.
- `AnimatorModel` implements `<IAnimatorModel>`:\
    implements all methods mandated in both interfaces.

This lets our View has-a IReadableModel, which allow View to only read the data from the model and parse the data
into specific output format, but cannot change the data. This will be convenience and also achieve data protection.

2. Add/delete methods: more flexible and solve constraints
- Add `getShapeIDs` :
    returns name list of all shapes in this animation, with the knowledge of name one can access data of a specific shape easily.
- Add `getMotions` :
    allow access the list of transformations of a shape by its id.
- Add `getCanvas` and `setCanvas`:
    the display (in view) of actual animation needs a data to represent canvas/screen. size.
- Add ‘`addAction`’ and delete ‘move’, ‘scale’, and ‘color’ :
    allow adding different kinds of motions flexibly. It will be less coupling and one don’t have to know what kind of act it is,
    just let the model classify and validate it. It supports shape change various attributes at the SAME time period.
- Delete `setDisplay`:
    Last version, shape must make motion during its display time, the display time must be set before doing any transformation.
    This version, appear and disappear time update dynamically according to the time of motion, more flexible.
- Rename `toString` to `getState`:
    more straightforward to tell this method is to output the whole state of the animation.

3. Implements <AnimationBuilder> inside AnimatorModel:
Associate with AnimationReader, it allows read data from a file and parse into the builder, which can build a model directly.

### **View**

---------------------------
**I. Description:** 
Represent the different output types/formats of the animation.
A view processes data from IReadableModel and produce output depends on its implementation.
It can also display the model's animation at a certain speed (number of ticks per second).
So far, it supports three types of view: visual, SVG and text. It will support the GUI view in the future.

**II. Support operations:**
- `void render(int tempo)`:\
    Render the animation in specific speed. Different view will have different way to "show" the view.
    TextView and SVGView output depends on its Appendable, VisualView will be given a signal to repaint itself.
- `void makeVisible()`:\
    Make the visual view and GUI view visible. Usually called after the view set up.
    SVG and Textview will do nothing when this method evoked.
- ~~void setListener(ActionListener actionEvent):\
    Provide the view with an action listener and let controller controls the production of animation in view.
    Throw OperationNotSupportedException when if view not support this operation.~~


**III. Details of each view:**
0. `AbstractView` implements `<IView>`\
It represents view that output animation in a textual format.

1. `TextView` extends `AbstractView`\
Initialize view with _IReadableModel and Appendable_.It displays animation in text format.
All shape data will be sorted by the presenting time. All transformations' description will be sorted too.

    Output demo speed = 1:
        
        Speed: 1 tick(s) / second
    
        Shapes:
        Name: disk1
        Type: Rectangle
        Reference: (190.0, 180.0), Width: 20.0, Height: 30.0, Color: (0, 49, 90)
        Appears at t=1.0s, disappears at t=302.0s
    
        Shape disk1 move from (190.0, 180.0) to (190.0, 50.0), from t=25.0s to t=35.0s
        Shape disk1 move from (190.0, 50.0) to (490.0, 50.0), from t=36.0s to t=46.0s
        Shape disk1 move from (490.0, 50.0) to (490.0, 240.0), from t=47.0s to t=57.0s
        ...

    Output demo speed = 5:
   
        Speed: 5 tick(s) / second
    
        Shapes:
        Name: disk1
        Type: Rectangle
        Reference: (190.0, 180.0), Width: 20.0, Height: 30.0, Color: (0, 49, 90)
        Appears at t=0.2s, disappears at t=60.4s
    
        Shape disk1 move from (190.0, 180.0) to (190.0, 50.0), from t=5.0s to t=7.0s
        Shape disk1 move from (190.0, 50.0) to (490.0, 50.0), from t=7.2s to t=9.2s
        Shape disk1 move from (490.0, 50.0) to (490.0, 240.0), from t=9.4s to t=11.4s
        ...


2. `SVGView` extends `AbstractView`
It displays animation in svg format. Initialize view with _IReadableModel_ and _Appendable_.

    Output demo speed = 1:
    
        <rect id="disk3" x="145" y="240" width="110" height="30" fill="rgb(11,45,175)" visibility="visible" >
            <animate attributeType="xml" begin="121000.0ms" dur="10000.0ms" attributeName="y" from="240.0" to="50.0" fill="freeze" />
            <animate attributeType="xml" begin="132000.0ms" dur="10000.0ms" attributeName="x" from="145.0" to="445.0" fill="freeze" />
    
        </rect>
        ...
    
    Output demo speed = 5:
    
        <rect id="disk3" x="145" y="240" width="110" height="30" fill="rgb(11,45,175)" visibility="visible" >
            <animate attributeType="xml" begin="24200.0ms" dur="2000.0ms" attributeName="y" from="240.0" to="50.0" fill="freeze" />
            <animate attributeType="xml" begin="26400.0ms" dur="2000.0ms" attributeName="x" from="145.0" to="445.0" fill="freeze" />
    
        </rect>
        ...

3. `VisualView` extends `JFrame` implements `<IView>`\
It has-a _IReadableModel_ also has-a _AnimatorPanel_ extends JPanel:
A drawing class called by VisualView to draw the shapes in each tick.
Using getShapeAtTick method in IReadableModel. It controls animation speed by:
   
        public void render(int tempo) throws IllegalArgumentException {
            ActionListener listener = new ActionListener() {
            ...
              @Override
              public void actionPerformed(ActionEvent e) {
                ...
              }
            };
            new Timer(delay, listener).start(); 
       }


**IV. How it works:**
1. Using ViewFactory to create an instance with a IReadableModel and Appendable(except VisualView)
2. call makeVisible, set the frame visible(for VisualView, other will do nothing)
3. call render method with specific tempo, it will output/display the view

_***V. Upgrade our Views in A8: what changed and why?**_
1. Fix bugs: in last assignment, there's bug in SVG view since we did not deal with the canvas
   size properly, now it is fixed by setting the "viewBox" value.

2. Add enum named TypeOfView contains all view types we have so far and add a method getType()
   in the <IView> interface in order to help identify the view.

3. Since additional operations is needed, we develop an interface <IGUIView> extends <IView>,
which has two methods:
  - void setListener(IFeature feature):\
   Provide the view with a controller that implement IFeature, which contains all operations
    expected to make in the GUIView. (p.s. this was in IView interface in the previous version,
    since all other three will not support this operation, we decide to remove it from IView interface)
    
  - void draw(List<IShape2D> shapes):\
  update the view with list of shape to be drawn, usually passed by a controller.

4. Add an editor view called `GUIView`, allow user to actually "play" the animation, operation
   including start, pause, resume, restart, loop/stop loop, and speed change. So cool!
   Brief intro to GUIView:

  - Great design pattern and reduce code duplication:
  Thanks to the idea of composition and delegation, the GUIView has-a Visual view as component 
`    public GUIView(VisualView view)` so just delegate the operation to visual view, the GUIView just add some buttons and connect
  with the controller to achieve interaction with user.

  - Clean design, easy to operate but sufficiently functional:\
  we have 3 pairs of buttons and 1 list:\
  _i. start & restart:_\
    press the start button, the animation will start playing, the text
  on the button will become 'restart', which allow user to restart playing the animation anytime.\
  _ii. pause & resume_:\
    press the pause button to stop the animation, and the text on the button
  will become 'resume', press it again to start playing from where it stopped, the button change
  back to pause.\
  _iii. loop & stop loop_: \
    similar design to "pause & resume", it allows user to enable or disable
  loop playing of animation.\
  _iv. List of speed_: \
    0.5x, 0.75x, 1x, 1.25x .... these are factor to change the speed of playing.
  If factor less than 1 is to decrease speed, bigger than 1 is to increase speed and 1 is to
  set to the initial speed.\
  (Notice that user can see a message shows what they did/how they change the animation.)
    
    Here's a demo of the UI:\
        ![Image text](user_interface_speed.jpg)
    
    See more the user interface demo in the resources file, it is awesome!

### **Controller**

---------------------------
The Controller implement IController interface and IFeature interface. It has
   IAnimatorModel and IView as components and control the flow of animation production. For
   GUIView, it uses the swing Timer to control the speed and display of the animation and can
   support whatever features required by the view in IFeatures.

 Simple operation, to connect MVC you just need:

    ...(assume model and view already set up)...

      IController controller = new ControllerImpl(model, view);
      controller.run(speed);

done!

if you need to output a textual file (for `TextView`/`SVGView`), a few more steps need in main():

    ...
          if ((viewType.equals("text") || viewType.equals("svg"))
              && ap instanceof FileWriter) {
            ((FileWriter) ap).flush();
            ((FileWriter) ap).close();
          }
    ...

How it controls GUI and speed?

       // set up control to GUIView
       if (view.getViewType().equals(TypeOfView.GUI)) {
         ((IGUIView) view).setListener(this);
         initTempo = 1000 / speed;
         this.timer = new Timer(initTempo, actionEvent -> {
           if (repeat && tick > model.getLength()) {
             tick = 0;
           } else if (!repeat && tick > model.getLength()) {
             return;
           }
           ((IGUIView) view).draw(model.getShapeAtTick(tick));
           tick++;
         });
       }
     }
plus implements the methods in `IFeature` which is to control the timer(start, stop, setDelay...)
     therefore control the speed of passing shapes data to GUIView, so the GUIView will paint
     the shape in specific tempo.

 What is `<IFeature>`, how it works and why?
   - What and how?
   1. We encapsulate each of the features as a callback function in a common interface.
   2. Then the controller provides an object that implements the above interface to the view
    (instead of actual listeners).
   3. The view defines its own listeners, and then connects each listener to the appropriate callback function.
   4. When a button is clicked, the listener attached to the button calls the appropriate callback,
    thereby giving control to the controller.

   - Why? \
     The reason is simple: less coupling!\
   The controller no need to know how the specific detail of view's implementation.
   Also, since these stuff (change speed, start, pause...) are NOT the responsibility of a view,
   so the view no need to care about how to display and receive command from user, now the view
   just needs to accept the control from controller, and when the button's action listener receive
   command, it will ask the controller to do the reaction, for instance in GUIView:
     
           ...
               startButton.addActionListener(e -> {
                 if (startButton.getText().equals("start")) {
                   feature.start();
                   startButton.setText("restart");
                 } else if (startButton.getText().equals("restart")) {
                   feature.restart();
                 }
               });
            ...

- notice that the 'feature' here is the controller
---------------------------

 We learn a lot in this assignment. The MVC structure is really helpful especially when we have so many data
 to manage and have a lot of operations. The SOLID principle also really help when we are thinking about how
 to design each part even each class. Anyway, we are really proud of ourselves to complete such a big project,
 this is such a gift for both of us as an Aligner to CS industry.
