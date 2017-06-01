package ru.nsu.fit.g14203.popov.raytracing.tracing;

import ru.nsu.fit.g14203.popov.wireframe.Figure3D;
import ru.nsu.fit.g14203.popov.wireframe.matrix.Vector;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

public abstract class RealFigure3D extends Figure3D {

    class ContactPoint {
        Vector pos;
        Vector norm;

        Ray income;
        Ray reflected;

        ContactPoint(Vector pos, Vector norm, Ray reflected) {
            this.pos = pos;
            this.norm = norm;

            this.reflected = reflected;
            income = new Ray(pos, reflected.direction.copy()
                            .shift(norm.copy().resize(-2 * norm.dotProduct(reflected.direction))));
        }

        RealFigure3D getFigure() {
            return RealFigure3D.this;
        }

        boolean equals(ContactPoint other) {
            return other != null &&
                    pos.distance(other.pos) <= 0.001 &&
                    norm.distance(other.norm) <= 0.001;

        }
    }

    private float[] diffuseModifier;
    private float[] specularModifier;
    private float shiny;

    RealFigure3D(float[] diffuseModifier, float[] specularModifier, float shiny) {
        this.diffuseModifier = diffuseModifier;
        this.specularModifier = specularModifier;
        this.shiny = shiny;
    }

    void setReflectedIntense(Collection<Light> lights, Collection<RealFigure3D> figures,
                             float ambientIntense[], ContactPoint contactPoint) {
        float[] intense = new float[3];
        for (int i = 0; i < 3; i++) {
            intense[i] = contactPoint.income.intense[i] * specularModifier[i] +
                         ambientIntense[i] * diffuseModifier[i];
        }

        for (Light light : lights) {
            Vector lightDirection = contactPoint.pos.copy()
                    .shift(light.getPosition().copy().resize(-1))
                    .normalize();
            Ray lightStraight = new Ray(light.getPosition(), lightDirection);

            ContactPoint crossPoint = figures.stream()
                    .map(figure -> figure.contact(lightStraight))
                    .filter(Objects::nonNull)
                    .min(Comparator.comparingDouble(cPoint ->
                            cPoint.pos.distance(light.getPosition()))).orElse(null);
            if (!contactPoint.equals(crossPoint))
                continue;


            double lightDistance = light.getPosition().distance(contactPoint.pos);
            float lightModifier = (float) (1.0 / (1.0 + lightDistance));

            double tmp = contactPoint.norm.dotProduct(lightDirection.copy()
                    .shift(contactPoint.reflected.direction)
                    .normalize());
            float diffuse = (float) contactPoint.norm.dotProduct(lightDirection);
            float specular = (float) Math.pow(tmp, shiny);

            for (int i = 0; i < 3; i++) {
                intense[i] += light.getIntense()[i] * lightModifier *
                        (diffuseModifier[i] * diffuse + specularModifier[i] * specular);
            }
        }

        contactPoint.reflected.intense = intense;
    }

    abstract ContactPoint contact(Ray income);
}
