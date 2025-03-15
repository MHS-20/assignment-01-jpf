### Version 1

Provare a fare un thread per ogni boid, ognuno calcola il proprio nuovo stato in parallelo. Quando tutti hanno finito di leggere e calcolare (barrier), posso aggiornarsi tutti in parllelo, perché ognuno aggiorna sé stesso e non ci sono conflitti.

Provando senza rendere Boid Runnable, è più lento di prima, forse perché crei nuovi thread per ogni boid ad ogni iterazione. La lista di Boid può diventare una lista di thread.

In Boids class, operations can be made parallel:

```
List<Boid> nearbyBoids = getNearbyBoids(model);
V2d separation = calculateSeparation(nearbyBoids, model);
V2d alignment = calculateAlignment(nearbyBoids, model);
V2d cohesion = calculateCohesion(nearbyBoids, model);
```

Also the Boid Panel is sequential:

```
for (Boid boid : boids) {
var x = boid.getPos().x();
var y = boid.getPos().y();
int px = (int)(w/2 + x*xScale);
int py = (int)(h/2 - y*xScale);
g.fillOval(px,py, 5, 5); // is this the bottleneck?
}
```

The start/stop button has to stop all threads, but keeping a thread array it's straightforward.

