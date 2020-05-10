package it.cubicworldsimulator.engine.graphic;

import it.cubicworldsimulator.engine.Utils;
import it.cubicworldsimulator.engine.loader.Loader;
import it.cubicworldsimulator.engine.loader.TextureLoaderImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OBJLoader {
    private final static Logger logger = LogManager.getLogger(OBJLoader.class);
    private final List<Vector3f> vertices;
    private final List<Vector2f> textures;
    private final List<Vector3f> normals;
    private final List<Face> faces;
    private String textureFileName;

    public OBJLoader() {
        this.vertices = new ArrayList<>();
        this.textures = new ArrayList<>();
        this.normals = new ArrayList<>();
        this.faces = new ArrayList<>();
    }

    public Mesh loadFromOBJ(String objFileName, String textureFileName) throws Exception {
        this.textureFileName = textureFileName;
        final List<String> lines = Utils.readAllLines(objFileName);
        for (String line : lines) {
            final List<String> tokens = List.of(line.split("\\s+"));
            switch (tokens.get(0)) {
                case "v":
                    // Geometric vertex
                    Vector3f vec3f = new Vector3f(
                            Float.parseFloat(tokens.get(1)),
                            Float.parseFloat(tokens.get(2)),
                            Float.parseFloat(tokens.get(3)));
                    vertices.add(vec3f);
                    break;
                case "vt":
                    // Texture coordinate
                    Vector2f vec2f = new Vector2f(
                            Float.parseFloat(tokens.get(1)),
                            Float.parseFloat(tokens.get(2)));
                    textures.add(vec2f);
                    break;
                case "vn":
                    // Vertex normal
                    Vector3f vec3fNorm = new Vector3f(
                            Float.parseFloat(tokens.get(1)),
                            Float.parseFloat(tokens.get(2)),
                            Float.parseFloat(tokens.get(3)));
                    normals.add(vec3fNorm);
                    break;
                case "f":
                    final Face face = new Face(tokens.get(1), tokens.get(2), tokens.get(3));
                    faces.add(face);
                    break;
                default:
                    // Ignore other lines
                    break;
            }
        }
        return this.reorderLists();
    }

    private Mesh reorderLists() throws Exception {
        List<Integer> indices = new ArrayList<>();
        // Create position array in the order it has been declared
        float[] posArr = new float[this.vertices.size() * 3];
        int i = 0;
        for (final Vector3f pos : this.vertices) {
            posArr[i * 3] = pos.x();
            posArr[i * 3 + 1] = pos.y();
            posArr[i * 3 + 2] = pos.z();
            i++;
        }
        float[] textCoordArr = new float[this.vertices.size() * 2];
        float[] normArr = new float[this.vertices.size() * 3];

        for (Face face : this.faces) {
            final List<IdxGroup> faceVertexIndices = face.getFaceVertexIndices();
            for (final IdxGroup indValue : faceVertexIndices) {
                processFaceVertex(indValue, this.textures, this.normals,
                        indices, textCoordArr, normArr);
            }
        }
        int[] indicesArr = indices.stream()
                            .mapToInt((Integer v) -> v)
                            .toArray();
        logger.debug("Vertices: " + posArr.length);
        logger.debug("UVs: " + textCoordArr.length);
        logger.debug("Indices: " + indicesArr.length);

        return new Loader().createMesh(posArr, textCoordArr, indicesArr, normArr,
                new Material(new TextureLoaderImpl().loadTexture(textureFileName)), 0);
    }

    private void processFaceVertex(final IdxGroup indices, final List<Vector2f> textCoordList,
                                          final List<Vector3f> normList, final List<Integer> indicesList,
                                          final float[] texCoordArr, final float[] normArr) {
        // Set index for vertex coordinates
        final int posIndex = indices.getIdxPos();
        indicesList.add(posIndex);

        // Reorder texture coordinates
        if (indices.getIdxTextCoord() >= 0) {
            final Vector2f textCoord = textCoordList.get(indices.getIdxTextCoord());
            texCoordArr[posIndex * 2] = textCoord.x();
            texCoordArr[posIndex * 2 + 1] = 1 - textCoord.y();
        }
        if (indices.getIdxVecNormal() >= 0) {
            // Reorder normal vectors
            final Vector3f vecNorm = normList.get(indices.getIdxVecNormal());
            normArr[posIndex * 3] = vecNorm.x();
            normArr[posIndex * 3 + 1] = vecNorm.y();
            normArr[posIndex * 3 + 2] = vecNorm.z();
        }
    }

    /*Inner class*/
    private static class Face {
        /**
         * List of idxGroup groups for a face triangle (3 vertices per face).
         */
        private final List<IdxGroup> indexGroups;

        public Face(final String v1, final String v2, final String v3) {
            this.indexGroups = new ArrayList<>();
            // Parse the lines
            this.indexGroups.add(parseLine(v1));
            this.indexGroups.add(parseLine(v2));
            this.indexGroups.add(parseLine(v3));
        }

        public List<IdxGroup> getFaceVertexIndices() {
            return this.indexGroups;
        }

        private IdxGroup parseLine(final String line) {
            Objects.requireNonNull(line, "Line missing!");
            IdxGroup idxGroup = new IdxGroup();
            List<String> lineTokens = List.of(line.split("/"));
            int length = lineTokens.size();
            idxGroup.setIdxPos(Integer.parseInt(lineTokens.get(0)) - 1);
            if (length > 1) {
                // It can be empty if the obj does not define text coords
                String textCoord = lineTokens.get(1);
                idxGroup.setIdxTextCoord(textCoord.length() > 0 ? Integer.parseInt(textCoord) - 1 : IdxGroup.NO_VALUE);
                if (length > 2) {
                    idxGroup.setIdxVecNormal(Integer.parseInt(lineTokens.get(2)) - 1);
                }
            }
            return idxGroup;
        }
    }
    private static class IdxGroup {
        public static final int NO_VALUE = -1;
        private int idxPos;
        private int idxTextCoord;
        private int idxVecNormal;

        public IdxGroup() {
            this.idxPos = NO_VALUE;
            this.idxTextCoord = NO_VALUE;
            this.idxVecNormal = NO_VALUE;
        }

        public int getIdxPos() {
            return idxPos;
        }

        public void setIdxPos(int idxPos) {
            this.idxPos = idxPos;
        }

        public int getIdxTextCoord() {
            return idxTextCoord;
        }

        public void setIdxTextCoord(int idxTextCoord) {
            this.idxTextCoord = idxTextCoord;
        }

        public int getIdxVecNormal() {
            return idxVecNormal;
        }

        public void setIdxVecNormal(int idxVecNormal) {
            this.idxVecNormal = idxVecNormal;
        }
    }
}