### Version 1

Should I make a thread for each boid? When a boid need to update itself in the shared array, it requires a lock. We can have concurrent readers and concurrent writers, but not a mix of them, because writers always write different boids.

Therefore, I can use a barrier, wait for all threads to compute their new state (everybody reads) and then everybody writes its new state. But is this the correct way to update the simulation, or we are supposed to make the new state available to other boids asap?

In Boids class, after getNearbyBoids, other operations can be made parallel:

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

