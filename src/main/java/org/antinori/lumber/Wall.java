package org.antinori.lumber;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class Wall {

    private List<Lumber> bottomPieces = new ArrayList<Lumber>();
    private List<Lumber> topPieces = new ArrayList<Lumber>();
    private List<Lumber> verticalPieces = new ArrayList<Lumber>();

    private boolean sideWall = false;
    private boolean use2x6 = false;

    private double widthFeet;
    private double heightFeet;

    private double widthInches;
    private double heightInches;

    private Vector3 centerPos;
    private Vector3 cornerPos;

    private ModelInstance instance;

    public enum LumberLength {

        EIGHT, TEN, TWELVE, SIXTEEN
    };

    public Wall(double width, double height, boolean side, boolean use2x6) throws Exception {

        this.sideWall = side;
        this.use2x6 = use2x6;

        this.widthFeet = width;
        this.heightFeet = height;

        double inches = convertFeetToInches(widthFeet);
        this.widthInches = sideWall ? inches - (use2x6 ? 5.5f * 2 : 3.5f * 2) : inches;

        this.heightInches = convertFeetToInches(heightFeet);

        setLumber();
    }

    public void setLumber() throws Exception {
        setHorizontalLumber(topPieces);
        setHorizontalLumber(bottomPieces);
        setVerticalLumber(verticalPieces);
    }

    private void setVerticalLumber(List<Lumber> list) throws Exception {

        //spaced 16 inches on center
        double floor = Math.floor(widthInches / 16);
        double leftover = widthInches % 16;

        LumberType lt = getType(LumberLength.EIGHT);
        if (heightFeet <= 8) {
            lt = getType(LumberLength.EIGHT);
        } else if (heightFeet <= 10) {
            lt = getType(LumberLength.TEN);
        } else if (heightFeet <= 12) {
            lt = getType(LumberLength.TWELVE);
        } else if (heightFeet <= 16) {
            lt = getType(LumberLength.SIXTEEN);
        } else {
            throw new Exception("Cannot make height more than 16 feet.");
        }

        //add first one
        list.add(new Lumber(lt, heightInches - 3));

        for (int x = 0; x < (int) floor; x++) {
            list.add(new Lumber(lt, heightInches - 3));
        }

        if (leftover > 1.5f) {
            list.add(new Lumber(lt, heightInches - 3));
        }

    }

    private void setHorizontalLumber(List<Lumber> list) {

        list.clear();

        if (widthFeet <= 8) {
            list.add(new Lumber(getType(LumberLength.EIGHT), widthInches));
        } else if (widthFeet <= 10) {
            list.add(new Lumber(getType(LumberLength.TEN), widthInches));
        } else if (widthFeet <= 12) {
            list.add(new Lumber(getType(LumberLength.TWELVE), widthInches));
        } else if (widthFeet <= 16) {
            list.add(new Lumber(getType(LumberLength.SIXTEEN), widthInches));
        } else {

            double floor = Math.floor(widthFeet / 16);
            double leftover = widthFeet % 16;

            for (int x = 0; x < (int) floor; x++) {
                list.add(new Lumber(getType(LumberLength.SIXTEEN), 16 * 12));
            }

            if (leftover > 0) {
                if (leftover <= 8) {
                    list.add(new Lumber(getType(LumberLength.EIGHT), leftover * 12));
                } else if (leftover <= 10) {
                    list.add(new Lumber(getType(LumberLength.TEN), leftover * 12));
                } else if (leftover <= 12) {
                    list.add(new Lumber(getType(LumberLength.TWELVE), leftover * 12));
                } else {
                    list.add(new Lumber(getType(LumberLength.SIXTEEN), leftover * 12));
                }
            }

            if (sideWall) {
                Lumber last = list.get(0);
                last.setLength(last.getLength() - (use2x6 ? 5.5f * 2 : 3.5f * 2));
            }

        }

    }

    public LumberType getType(LumberLength l) {

        LumberType lt = LumberType.LUM_2x4x8;

        switch (l) {
            case EIGHT:
                lt = use2x6 ? LumberType.LUM_2x6x8 : LumberType.LUM_2x4x8;
                break;
            case SIXTEEN:
                lt = use2x6 ? LumberType.LUM_2x6x16 : LumberType.LUM_2x4x16;
                break;
            case TEN:
                lt = use2x6 ? LumberType.LUM_2x6x10 : LumberType.LUM_2x4x10;
                break;
            case TWELVE:
                lt = use2x6 ? LumberType.LUM_2x6x12 : LumberType.LUM_2x4x12;
                break;
            default:
                break;
        }

        return lt;
    }

    public float getCost() {
        float cost = 0;

        for (Lumber l : topPieces) {
            cost += l.getType().getCost();
        }

        for (Lumber l : bottomPieces) {
            cost += l.getType().getCost();
        }

        for (Lumber l : verticalPieces) {
            cost += l.getType().getCost();
        }

        return cost;
    }

    public static double convertFeetToInches(double feet) {
        double floor = Math.floor(feet);
        double leftoverInches = (feet - floor) * 12;
        double inches = floor * 12 + leftoverInches;
        return inches;
    }

    @Override
    public String toString() {

        StringBuffer sb = new StringBuffer(String.format("Wall %s (%s) x %s (%s)", widthFeet, widthInches, heightFeet, heightInches));

        sb.append("\n\tTOP");
        for (Lumber l : topPieces) {
            sb.append("\n\t\t" + l.toString());
        }

        sb.append("\n\tBOTTOM");
        for (Lumber l : bottomPieces) {
            sb.append("\n\t\t" + l.toString());
        }

        sb.append("\n\tVERTICAL");
        Lumber v = verticalPieces.get(0);
        sb.append("\n\t\t" + v.toString() + " x (" + verticalPieces.size() + ")");

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String moneyString = formatter.format(getCost());

        sb.append("\n\tCOST: " + moneyString);

        return sb.toString();
    }

    public List<Lumber> getBottomPieces() {
        return bottomPieces;
    }

    public List<Lumber> getTopPieces() {
        return topPieces;
    }

    public List<Lumber> getVerticalPieces() {
        return verticalPieces;
    }

    public boolean isSideWall() {
        return sideWall;
    }

    public boolean isUse2x6() {
        return use2x6;
    }

    public double getWidthFeet() {
        return widthFeet;
    }

    public double getHeightFeet() {
        return heightFeet;
    }

    public double getWidthInches() {
        return widthInches;
    }

    public double getHeightInches() {
        return heightInches;
    }

    public Vector3 getCenterPos() {
        return centerPos;
    }

    public void setCenterPos(Vector3 centerPos) {
        this.centerPos = centerPos;
    }

    public Vector3 getCornerPos() {
        return cornerPos;
    }

    public void setCornerPos(Vector3 cornerPos) {
        this.cornerPos = cornerPos;
    }

    public ModelInstance getInstance() {
        return instance;
    }

    public void setInstance(ModelInstance instance) {
        this.instance = instance;
    }

}
