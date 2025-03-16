### Version 1

Usare N thread per spezzare la lista di Boid, ed ognuno si occupa di un sottogruppo, però devi mettere un mutex su ogni boid per aggiornarlo, perché potrebbe essere che quel boid venga letto da un altro thread.

- Synchronized nel model serve?
- Più reader possono accedere ad un boid, ma il writer deve essere solo.
- Dividere tra Reader & Writer class? Altrimenti come li sincronizzi bene?
- Parallelizzare il rendering di ogni boid nella GUI?
- Per ora il tasto start/stop funziona perché i thread terminano dopo un giro, se invece li fai long-lived devi fermarli uno per volta e sincronizzarli in modo che si aspettino alla fine di ogni iterazione (barrier)

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
