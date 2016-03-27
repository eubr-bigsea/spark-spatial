/* SimpleApp.scala */
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

object SimpleApp {
  def main(args: Array[String]) {
    val logFile = args(0)
    val conf = new SparkConf().
      setAppName("Simple Application")
    // com sc você pode setar qualquer configuração passada na linha de comando
    // mas isso pode não ser uma boa ideia (por portabilidade)
    // por exemplo, --executor-memory
    // .set("spark.executor.memory", "1g")
    // isso é no caso do modo cluster, naturalmente
    
    // criando o SparkContext
    val sc = new SparkContext(conf)
    
    // esse exemplo lê um arquivo e conta linhas com 'a' e linhas com 'b'
    val logData = sc.textFile(logFile, 2).cache()
    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    
    println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))

    sc.stop()
  }
}

/*
- Uma possibilidade de executar essa aplicação é: supondo que isso foi compilado para
SimpleApp.jar
- Substitua <caminho> em file://<caminho> com o caminho com completo do arquivo
- por exemplo: /home/vagrant/spark/README.md

- Modo cluster (ou seja, você NÃO irá executar assim dentro da VM vagrant)
$ ./bin/spark-submit \
    --class test.apps.SimpleApp \
    --master spark://master:7077 \
    --executor-memory 6G \
    --executor-cores 8 \
    SimpleApp.jar \
    file:///caminho/para/um/arquivo/local

- Modo local (utilize esta chamada para a execução na VM)
$ ./bin/spark-submit \
    --class test.apps.SimpleApp \
    --master local[2] \
    SimpleApp.jar \
    file:///caminho/para/um/arquivo/local
 */
