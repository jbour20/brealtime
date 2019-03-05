package jeffbour.brealtime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Spark;

public class Application
{
  public static void main(String[] args)
  {
    Spark.get("/question", (request, response) -> {

      LOG.info(request.queryString());

      String question = request.queryParams("q");
      String details = request.queryParams("d");
      String answer = QuestionAndAnswer.answer(question, details);

      response.header("Content-Type", "text/plain; charset=utf-8");

      return answer;

    });
  }

  private final static Logger LOG = LoggerFactory.getLogger(Application.class);
}
