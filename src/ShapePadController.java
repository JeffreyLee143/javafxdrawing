// import java.lang.Math.*; //有使用Math.abs(), Math.Min(), Math.Max(), 若是IDE沒有默認導入則需手動導入。
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class ShapePadController {

    // 抽象形狀類別
    abstract static class MyShape {

        protected double x1, y1, x2, y2;  // 形狀的一角及另一角(x,y)座標
        protected Color color; // 形狀顏色
        protected boolean filled; // 形狀是否填滿

        // 建構子
        public MyShape(double x1, double y1, double x2, double y2, Color color, boolean filled) {
            this.x1=x1;
            this.x2=x2;
            this.y1=y1;
            this.y2=y2;
            this.color=color;
            this.filled=filled;
          // 填寫形狀屬性初始值
        }

        public double get_Width(){
            // 絕對值算寬度
            return Math.abs(x1-x2);
        }

        public double get_Height(){
            // 絕對值算長度
            return Math.abs(y1-y2);
        }
        // 形狀繪圖方法，待後代形狀補齊此家規
        public abstract void draw(GraphicsContext gc);
    }

    // 矩形類別
    static class MyRectangle extends MyShape {
        
        // 建構子
        public MyRectangle(double x1, double y1, double x2, double y2, 
                           Color color, boolean filled) {
            super(x1, y1, x2, y2, color, filled);
        }

        @Override // 矩形繪圖方法
        public void draw(GraphicsContext gc) {
            if(this.filled){
                // 有填滿的情況
                gc.setFill(this.color);
                // 依據形狀的 color, filled, x1, y1, x2, y2 屬性在畫布繪製矩形
                // 確保起始座標是在左上角
                gc.fillRect(Math.min(this.x1,this.x2), Math.min(this.y1,this.y2), this.get_Width(), this.get_Height());
            }else{
                // 沒填滿的情況
                gc.setStroke(this.color); // 設定邊框顏色
                gc.setLineWidth(2); // 設定邊框寬度
                // 確保起始座標是在左上角
                gc.strokeRect(Math.min(this.x1,this.x2), Math.min(this.y1,this.y2), this.get_Width(), this.get_Height()); // 繪製邊框
            } 
        }
    }

    // 圓形類別
    static class MyCircle extends MyShape {
        // 建構子
        public MyCircle(double x1, double y1, double x2, double y2, 
                        Color color, boolean filled) {
            super(x1, y1, x2, y2, color, filled);
        }

        @Override
        public void draw(GraphicsContext gc) {
            // 依據形狀的 color, filled, x1, y1, x2, y2 屬性在畫布繪製圓形
            double centerX = (this.x1 + this.x2) / 2; // 計算圓心X座標
            double centerY = (this.y1 + this.y2) / 2; // 計算圓心Y座標
            double radius = Math.abs(this.x2 - this.x1) / 2; // 計算半徑

            if (this.filled) {
                // 有填滿的情況
                gc.setFill(this.color); // 設定填充顏色
                gc.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2); // 填充圓形
            } else {
                // 沒填滿的情況
                gc.setStroke(this.color); // 設定邊框顏色
                gc.setLineWidth(2); // 設定邊框寬度
                gc.strokeOval(centerX - radius, centerY - radius, radius * 2, radius * 2); // 繪製圓形邊框
            }
        }
    }

    // 直線類別
    static class MyLine extends MyShape {
        // 建構子 
        public MyLine(double x1, double y1, double x2, double y2, 
                      Color color, boolean filled) {
            super(x1, y1, x2, y2, color, filled);
        }

        @Override
        public void draw(GraphicsContext gc) {
            // 依據形狀的 color, x1, y1, x2, y2 屬性在畫布繪製直線
            gc.setStroke(this.color);
            gc.setLineWidth(2);
            gc.strokeLine(this.x1, this.y1, this.x2, this.y2);
        }
    }

    // 橢圓類別
    static class MyEllipse extends MyShape {
        // 建構子
        public MyEllipse(double x1, double y1, double x2, double y2, Color color, boolean filled) {
            super(x1, y1, x2, y2, color, filled);
        }
        @Override
        public void draw(GraphicsContext gc) {
            double centerX = (this.x1 + this.x2) / 2; // 計算橢圓中心X座標
            double centerY = (this.y1 + this.y2) / 2; // 計算橢圓中心Y座標
            double width = Math.abs(this.x2 - this.x1); // 計算橢圓的寬度
            double height = Math.abs(this.y2 - this.y1); // 計算橢圓的高度

            if (this.filled) {
                gc.setFill(this.color); // 設定填充顏色
                gc.fillOval(centerX - width / 2, centerY - height / 2, width, height); // 填充橢圓
            } else {
                gc.setStroke(this.color); // 設定邊框顏色
                gc.setLineWidth(2); // 設定邊框寬度
                gc.strokeOval(centerX - width / 2, centerY - height / 2, width, height); // 繪製橢圓邊框
            }
        }
    }

    // 繪板元件等屬性
    @FXML
    private Canvas canvas;  // 繪圖畫布
    @FXML
    private final ArrayList<MyShape> shapes = new ArrayList<>(); // 形狀清單
    private MyShape currentShape = null; // 目前形狀
    private Color currentColor = Color.BLACK;  // 目前顏色
    private boolean filledShape = false;  // 是否填滿形狀
    private int shapeType = 0; // 形狀類型 0: Line, 1: Rectangle, 2: Circle, 3: Ellipse
    @FXML
    private Label statusLabel; // 狀態列
    @FXML
    private ComboBox<String> colorBox; // 顏色下拉盒
    @FXML
    private ComboBox<String> shapeBox; // 形狀下拉盒
    @FXML
    private CheckBox filledCheckBox; // 填滿勾選盒
    @FXML
    private Button undoButton;  // 還原按鈕
    @FXML
    private Button clearButton;  // 清除按鈕


    // 建立各元件的動作事件處理器
    public void initialize() 
    {
        // 註冊滑鼠事件處理器
        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseDragged(this::handleMouseDragged);
        canvas.setOnMouseReleased(this::handleMouseReleased);
        canvas.setOnMouseMoved(this::handleMouseMoved);
        
        // 初始化形狀(矩形)
        shapeType=0;

        // 把顏色放入清單內
        colorBox.getItems().addAll(
            "Red",   
            "Orange",   
            "Yellow",  
            "Green",   
            "Blue",  
            "Indigo",   
            "Purple",   
            "Black",   
            "White"    
        );

        // 把形狀放入清單內
        shapeBox.getItems().addAll(
            "Rectangle",
            "Circle",    
            "Line" ,
            "Ellipse"
        );

        // 默認選擇黑色
        colorBox.setValue("Black"); 

        // 默認選擇矩形
        shapeBox.setValue("Rectangle"); 

        // 還原按鈕事件處理器
        // 下面事件lambda parameter沒用到都表示成"_"避免跳出提醒
        undoButton.setOnAction(_ -> {
            if(!shapes.isEmpty()){
                shapes.removeLast(); // 刪除最後一個形狀
                redraw(canvas.getGraphicsContext2D());
            }
            // 若形狀集合不為空，則移除最後一個形狀，並重繪畫布
        });

        // 清除按鈕事件處理器
        clearButton.setOnAction(_ -> {
            // 清除形狀集合，並重繪畫布
            shapes.removeAll(shapes); 
            redraw(canvas.getGraphicsContext2D());
        });

        // 顏色下拉盒事件處理器
        colorBox.setOnAction(_ -> {
            String selectedColor = colorBox.getValue();
            // 依據顏色下拉盒選擇的選項字串，設定目前顏色 currentColor
            switch (selectedColor) {
                case "Red":
                    currentColor = Color.RED; // 紅色
                    break;
                case "Orange":
                    currentColor = Color.ORANGE; // 橙色
                    break;
                case "Yellow":
                    currentColor = Color.YELLOW; // 黃色
                    break;
                case "Green":
                    currentColor = Color.GREEN; // 綠色
                    break;
                case "Blue":
                    currentColor = Color.BLUE; // 藍色
                    break;
                case "Indigo":
                    currentColor = Color.INDIGO; // 靛
                    break;
                case "Violet":
                    currentColor = Color.VIOLET; // 紫色
                    break;
                case "Black":
                    currentColor = Color.BLACK; // 黑色
                    break;
                case "White":
                    currentColor = Color.WHITE; // 白色
                    break;
                default:
                    currentColor = Color.BLACK; // 預設黑色
                    break;
            }
        });

        // 形狀下拉盒事件處理器
        shapeBox.setOnAction(_ -> {
            String selectShapeType = shapeBox.getValue();
            // 依據形狀下拉盒選擇的選項索引，設定目前形狀類型 shapeType
            switch (selectShapeType) {
                case "Rectangle":
                    shapeType=0; // 矩形
                    break;
                case "Circle":
                    shapeType=1; // 圓形
                    break;
                case "Line":
                    shapeType=2; // 直線 
                    break;
                case "Ellipse":
                    shapeType=3; // 橢圓
                    break;
                default:
                    shapeType=0; // 預設矩形
                    break;
            }
        });

        // 填滿勾選盒事件處理器
        filledCheckBox.setOnAction(_ -> {
            // 設定是否填滿形狀 fillShape
            if(filledCheckBox.isSelected()){
                filledShape=true; // 有填滿
            }else{
                filledShape=false; // 沒填滿(框框)
            }
        }); 
    }

    // 畫布滑鼠壓下事件處理器
    private void handleMousePressed(MouseEvent e) {
        // 依據目前形狀類型 shapeType，建立對應形狀物件 currentShape
        double x1 = e.getX();
        double y1 = e.getY();
        switch (shapeType) {
            case 0: // 矩形
                currentShape = new MyRectangle(x1, y1, x1, y1, currentColor, filledShape);
                break;
            case 1: // 圓形
                currentShape = new MyCircle(x1, y1, x1, y1, currentColor, filledShape);
                break;
            case 2: // 直線
                currentShape = new MyLine(x1, y1, x1, y1, currentColor, filledShape);
                break;
            case 3: //橢圓
                currentShape = new MyEllipse(x1, y1, x1, y1, currentColor, filledShape);
                break;
            default:
                currentShape = new MyRectangle(x1, y1, x1, y1, currentColor, filledShape);
                break;
        }
    }

    // 畫布滑鼠拖曳事件處理器
    private void handleMouseDragged(MouseEvent e) {
        // 若目前形狀物件不為空，則設定其第二角(x,y)座標，並重繪畫布
        if (currentShape != null) {
            double x2 = e.getX();
            double y2 = e.getY();
            statusLabel.setText(String.format("X: %.2f ,Y: %.2f",x2,y2)); // 更新狀態標籤
            currentShape.x2 = x2; // 更新结束座標
            currentShape.y2 = y2; // 更新结束座標
            shapes.add(currentShape); // 將當前形狀添加到集合中
            redraw(canvas.getGraphicsContext2D()); // 重繪
            shapes.removeLast(); // 刪除最後一個形狀，否則會出現殘影的狀況
        }
    }

    // 畫布滑鼠放開事件處理器
    private void handleMouseReleased(MouseEvent e) {
        // 若目前形狀物件不為空，
        if (currentShape != null) {
            // 則設定其第二角(x,y)座標，加入形狀集合，
            double x2 = e.getX();
            double y2 = e.getY();
            currentShape.x2 = x2; // 更新结束座標
            currentShape.y2 = y2; // 更新结束座標
            shapes.add(currentShape); // 將當前形狀添加到集合中
            //   並重繪畫布，將目前形狀物件設為空
            currentShape = null; // 重置當前形状
            redraw(canvas.getGraphicsContext2D()); // 重新繪製畫布
        }
        
    }

    // 畫布滑鼠移動事件處理器
    private void handleMouseMoved(MouseEvent e) {
        // 顯示滑鼠座標於狀態列 statusLabel
        double x = e.getX();
        double y = e.getY();
        statusLabel.setText(String.format("X: %.2f ,Y: %.2f",x,y)); // 更新狀態標籤(左下角)
    }

    // 畫布重繪方法
    private void redraw(GraphicsContext gc) {
        // 清除畫布, 重繪形狀集合中的所有形狀。若目前形狀物件不為空，也重繪之
        gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // 清空畫布
        for (MyShape shape : shapes) {
            // 根據形狀的類型進行繪製，例如：
            // 矩形
            if (shape instanceof MyRectangle) {
                MyRectangle rect = (MyRectangle) shape;
                rect.draw(gc);
            } else if (shape instanceof MyCircle) {
            // 圓形
                MyCircle circle = (MyCircle) shape;
                circle.draw(gc);
            }else if(shape instanceof MyLine){
            // 直線
                MyLine line = (MyLine) shape;
                line.draw(gc);
            }else if(shape instanceof MyEllipse){
            // 橢圓
                MyEllipse ellipse = (MyEllipse) shape;
                ellipse.draw(gc);
            }
            
        }
    }
}