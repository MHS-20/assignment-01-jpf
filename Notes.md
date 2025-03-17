### Version 1

Usare N thread per spezzare la lista di Boid, ed ognuno si occupa di un sottogruppo, però devi mettere un mutex su ogni boid per aggiornarlo, perché potrebbe essere che quel boid venga letto da un altro thread.

- Synchronized nel model serve?
- Più reader possono accedere ad un boid, ma il writer deve essere solo. Dividere tra Reader & Writer class?
- Per ora il tasto start/stop funziona perché i thread terminano dopo un giro, se invece li fai long-lived devi fermarli uno per volta e sincronizzarli in modo che si aspettino alla fine di ogni iterazione (barrier)

VelocityWorker & PositionWorker hanno due barrier diverse? Oppure sono lo stesso worker con una barrier unica? Calcolano la velocità, poi si aspettano, poi la posizione, e tornano alla velocità quando la gui ha finito?

Con una barrier unica però si rischia che perdano il sync, per essere sicuri servirebbero due barrier diverse.



Listeners JPF:

[https://github.com/javapathfinder/jpf-core/tree/master/src/main/gov/nasa/jpf/listener](https://github.com/javapathfinder/jpf-core/tree/master/src/main/gov/nasa/jpf/listener)
