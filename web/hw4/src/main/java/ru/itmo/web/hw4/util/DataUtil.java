package ru.itmo.web.hw4.util;

import ru.itmo.web.hw4.model.Post;
import ru.itmo.web.hw4.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class DataUtil {
    private static final List<User> USERS = Arrays.asList(
            new User(1, "MikeMirzayanov", "Mike Mirzayanov"),
            new User(6, "pashka", "Pavel Mavrin"),
            new User(9, "geranazavr555", "Georgiy Nazarov"),
            new User(11, "tourist", "Gennady Korotkevich")
    );

    private static final List<Post> POSTS = Arrays.asList(
            new Post(1, "Ton Hack Challenge", "TON Hack Challenge is a one-day contest aimed at finding vulnerabilities in smart contracts written in FunC, the programming language native to The Open Network blockchain with a prize pool of 30,000 TON.", 1),
            new Post(2, "Codeforces Round #829", "Hi everybody,\n" +
                    "\n" +
                    "This Sunday there will be a XX Moscow Team Olympiad, high school students competition based in Moscow that is an elimination contest for All-Russian Team Olympiad. This contest is prepared by Moscow Olympiad Scientific Committee that you may know by Moscow Open Olympiad, Moscow Olympiad for Young Students and Metropolises Olympiad rounds 327, 342, 345, 376, 401, 433, 441, 466, 469, 507, 516, 541, 545, 567, 583, 594, 622, 626, 657, 680, 704, 707, 727, 751, 775, 802.\n" +
                    "The round will be held at Sunday, October 23, 2022 at 10:50 and will last for 2 hours. Note the unusual time of the round. Each division will have 6 problems. The round will be held according to the Codeforces rules and will be rated for both divisions.", 11),
            new Post(3, "Codeforces Round #830", "Hello, Codeforces!\n" +
                    "\n" +
                    "I am happy to invite you to my Codeforces Round #830 (Div. 2) which will be held at Sunday, October 23, 2022 at 13:05. The round will be rated for all the participants with rating strictly less than 2100 before Sunday, October 23, 2022 at 10:50.\n" +
                    "The tasks were created and prepared by 74TrAkToR. I would like to thank everyone who helped me a lot with round preparation.\n" +
                    "Coordinator Artyom123 for excellent communication and help with preparation.\n" +
                    "Testers gisp_zjz, Pointy, q-w-q-w-q, feecIe6418, nnv-nick, xiaoziya, Ormlis, huangzirui, Chenyu_Qiu, triple__a, gyh20, low_, voventa, tibinyte, Mr.Robot_28, Qingyu, RUSH_D_CAT, physics0523, Lucina, Rhodoks, Tekor, Milesian, Kalashnikov, Lokeo, mejiamejia, TomiokapEace, Chenyu_Qiu, AquaMoon, flowerletter for high-quality testing and valuable advices.\n" +
                    "As well as the testers who tested the CheReKOSH Team Olympiad — it was from the problems of this Olympiad that the round was composed: orz, DIvanCode, CAMOBAP31.\n" +
                    "MikeMirzayanov for amazing Codeforces and Polygon platforms.", 1)
    );

    public static void addData(HttpServletRequest request, Map<String, Object> data) {
        data.put("users", USERS);

        for (User user : USERS) {
            if (Long.toString(user.getId()).equals(request.getParameter("logged_user_id"))) {
                data.put("user", user);
            }
            if (Long.toString(user.getId()).equals(request.getParameter("user_id"))) {
                data.put("user", user);
            }
        }
        data.put("posts", POSTS);

        for (Post post : POSTS) {
            if (Long.toString(post.getId()).equals(request.getParameter("post_id"))) {
                data.put("post", post);
            }
            if (Long.toString(post.getUser_id()).equals(request.getParameter("user_id"))) {
                data.put("user_post", post);
            }
        }
    }

}
