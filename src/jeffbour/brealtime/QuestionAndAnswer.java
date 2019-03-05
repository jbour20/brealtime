package jeffbour.brealtime;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

enum QuestionAndAnswer
{
  PING("Ping")
  {
    @Override
    String answer(String details)
    {
      return "OK";
    }
  },
  NAME("Name")
  {
    @Override
    String answer(String details)
    {
      return "Jeff Bour";
    }
  },
  EMAIL("Email Address")
  {
    @Override
    String answer(String details)
    {
      return "jpb515@nyu.edu";
    }
  },
  PHONE("Phone")
  {
    @Override
    String answer(String details)
    {
      return "(408) 410-6765";
    }
  },
  POSITION("Position")
  {
    @Override
    String answer(String details)
    {
      return "I am not applying for any role";
    }
  },
  YEARS("Years")
  {
    @Override
    String answer(String details)
    {
      return "2.5";
    }
  },
  REFERRER("Referrer")
  {
    @Override
    String answer(String details)
    {
      return "Glendawg";
    }
  },
  DEGREE("Degree")
  {
    @Override
    String answer(String details)
    {
      return "M.S. Computer Science (New York University), " +
          "B.A. Mathematics (University of Rochester)";
    }
  },
  RESUME("Resume")
  {
    @Override
    String answer(String details)
    {
      return "https://www.linkedin.com/in/jeff-bour/";
    }
  },
  SOURCE("Source")
  {
    @Override
    String answer(String details)
    {
      return "https://github.com/jbour20/brealtime";
    }
  },
  STATUS("Status")
  {
    @Override
    String answer(String details)
    {
      return "Yes";
    }
  },
  PUZZLE("Puzzle")
  {
    @Override
    String answer(String details)
    {
      List<String> lines = Splitter.on('\n')
          .omitEmptyStrings()
          .splitToList(details.trim());

      char[] values = lines.get(1).trim().toCharArray();

      char[][] expressions = lines.subList(2, lines.size())
          .stream()
          .map(s -> s.trim().substring(1).toCharArray())
          .toArray(char[][]::new);

      int[] ranking = rank(expressions);

      return generate(values, ranking);
    }

    private int[] rank(char[][] expressions)
    {
      Queue<Tuple<Integer>> queue = new ArrayDeque<>();

      // Build and collect tuples
      for (int i = 0; i < expressions.length; i++) {
        char[] expression = expressions[i];
        for (int j = 0; j < expression.length; j++) {
          if (expression[j] == LESS_THAN) {
            queue.add(new Tuple<>(i, j));
          } else if (expression[j] == GREATER_THAN) {
            queue.add(new Tuple<>(j, i));
          }
        }
      }

      // Merge tuples
      // I'm assuming the given expressions define a strict total order
      // Otherwise might use a counter to prevent infinite loop
      while (queue.size() > 1) {
        Tuple<Integer> t1 = queue.remove();
        Tuple<Integer> t2 = queue.remove();
        if (t1.greatest().equals(t2.least())) {
          queue.add(Tuple.merge(t1, t2));
        } else if (t2.greatest().equals(t1.least())) {
          queue.add(Tuple.merge(t2, t1));
        } else {
          queue.add(t1);
          queue.add(t2);
        }
      }

      List<Integer> ordering = queue.remove().values();

      // Invert order to compute rank
      int[] ranking = new int[ordering.size()];
      for (int i = 0; i < ordering.size(); i++) {
        ranking[ordering.get(i)] = i;
      }

      return ranking;
    }

    private String generate(char[] values, int[] ranking)
    {
      List<String> parts = new ArrayList<>();
      parts.add(new String(values));

      for (int i = 0; i < values.length; i++) {

        StringBuilder builder = new StringBuilder().append(values[i]);

        for (int j = 0; j < values.length; j++) {
          if (i == j) {
            builder.append(EQUAL_TO);
          } else {
            builder.append(ranking[i] > ranking[j] ? GREATER_THAN : LESS_THAN);
          }
        }

        parts.add(builder.toString());

      }
      return Joiner.on(' ').join(parts);
    }

    private final char GREATER_THAN = '>';
    private final char LESS_THAN = '<';
    private final char EQUAL_TO = '=';
  },
  ;

  QuestionAndAnswer(String question)
  {
    this.question = question;
  }

  static String answer(String question, String details)
  {
    for (QuestionAndAnswer value : values()) {
      if (value.question().equals(question)) {
        return value.answer(details);
      }
    }
    return StringUtils.EMPTY;
  }

  String question()
  {
    return question;
  }

  abstract String answer(String details);

  private final String question;
}
