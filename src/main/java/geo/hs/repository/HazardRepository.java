package geo.hs.repository;

import geo.hs.algorithm.coordinate.ConvertSRID;
import geo.hs.model.hazard.Hazard;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.referencing.operation.TransformException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static geo.hs.config.ApplicationProperties.getProperty;

public class HazardRepository {
    private final ConvertSRID convertSRID;
    private final GeometryFactory geometryFactory;

    public HazardRepository() {
        convertSRID = new ConvertSRID();
        geometryFactory = JTSFactoryFinder.getGeometryFactory();
    }

    public void applyHazard(Connection conn, ArrayList<Hazard> lake, ArrayList<Hazard> turnel) throws SQLException {
        String query = getUpdateQuery();
        try (PreparedStatement pStmt = conn.prepareStatement(query)) {
            setObject(pStmt, lake);
            setObject(pStmt, turnel);
        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
        }
    }

    public void setObject(PreparedStatement pStmt, ArrayList<Hazard> hazards) throws SQLException {
        for (Hazard hazard : hazards) {
            try {
                Coordinate coords = convertSRID.convertCoordinate(hazard.getLongitude(), hazard.getLatitude());
                Point point = geometryFactory.createPoint(coords);
                pStmt.setInt(1, -9999);
                pStmt.setObject(2, point.toString());
                pStmt.addBatch();
            } catch (TransformException e) {
                e.printStackTrace();
            }
        }
        System.out.println("update records : " + pStmt.executeBatch().length);
        pStmt.clearBatch();
    }

    private String getUpdateQuery() {
        StringBuilder query = new StringBuilder("update public.");
        query.append(getProperty("shp.table"));
        query.append(" AS SHP");
        query.append(" set hillshade = ?");
        query.append(" where ST_Within(ST_GeometryFromText(?, 5179), SHP.the_geom)");
        return query.toString();
    }
}
