FROM affo/storm:1.0.2

ADD target/segreto.jar $STORM_HOME/lib/
ADD target/segreto.jar /

ENTRYPOINT [ "storm", "jar", "segreto.jar" ]
