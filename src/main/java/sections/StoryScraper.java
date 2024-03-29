package sections;

import memory.types.Date;
import memory.types.Staff;
import memory.types.Story;
import utilities.Logger;
import utilities.SectionName;
import utilities.Unescaper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public class StoryScraper {
    private String cookies;
    private int pageNum = 2;

    public StoryScraper() {
    }

    public ArrayList<Story> run(ArrayList<Staff> staffList, String cookies) throws URISyntaxException, IOException {
        this.cookies = cookies;

        ArrayList<Story> storyList = new ArrayList<>();

        for (Staff staff : staffList) {
            int staffID = staff.getId();
            pageNum = 2;
            storyList.addAll(scrape(staffID));
        }

        return storyList;
    }

    private ArrayList<Story> scrape(int staffID) throws URISyntaxException, IOException {
        ArrayList<Story> staffStoryList = new ArrayList<>();
        int numberOfStories = 0;

        Logger.log(SectionName.STORY, "Scraping Stories #" + staffID, false);
        String url = "https://shsthetorch.com/wp-admin/edit.php?post_type=post&author=" + staffID + "&paged=1";
//
        URLConnection connection = new URI(url).toURL().openConnection();
        connection.setRequestProperty("Accept-Charset", UTF_8.name());
        connection.setRequestProperty("Cookie", cookies);

        InputStream response = connection.getInputStream();
        Scanner scanner = new Scanner(response);
        String responseBody = scanner.useDelimiter("\\A").next();

//            <a class="next-page button" href=""><span class="screen-reader-text">Next page</span><span aria-hidden="true">›</span></a>


        responseBody = getFullBody(responseBody, staffID);

        while (responseBody.contains("post_title")) {
            String postSep = "<div class=\"post_title\">";
            int postSepPos = responseBody.indexOf(postSep);
            if (postSepPos == -1) {
                break;
            }
            int postLength = postSepPos + postSep.length();
            String[] split = responseBody.substring(postLength, postLength + 500).split("<", 2);
            String title = Unescaper.unescape(split[0]);

            String draftSep = "<div class=\"_status\">";
            int draftSepPos = split[1].indexOf(draftSep);
            int draftLength = draftSepPos + draftSep.length();
            String draft = split[1].substring(draftLength).split("<", 2)[0];

            //
            String yearSep = "<div class=\"aa\">";
            int yearSepPos = split[1].indexOf(yearSep);
            int yearLength = yearSepPos + yearSep.length();
            int year = Integer.parseInt(split[1].substring(yearLength).split("<", 2)[0].substring(2, 4));

            String monthSep = "<div class=\"mm\">";
            int monthSepPos = split[1].indexOf(monthSep);
            int monthLength = monthSepPos + monthSep.length();
            int month = Integer.parseInt(split[1].substring(monthLength).split("<", 2)[0]);

            responseBody = responseBody.substring(postSepPos + 500);

            if (!Objects.equals(draft, "draft")) {
                numberOfStories++;
                staffStoryList.add(new Story(staffID, title, new Date(month, year)));
            }
        }

        Logger.log(SectionName.SILENT, ": " + numberOfStories + " Stories found");
        return staffStoryList;
    }

    private String getFullBody(String responseBody, int staffID) throws IOException, URISyntaxException {
        String tell = "next-page button";
        if (responseBody.contains(tell)) {
            responseBody = responseBody.replaceAll(tell, " ");

            String url = "https://shsthetorch.com/wp-admin/edit.php?post_type=post&author=" + staffID + "&paged=" + pageNum;
            URLConnection connection = new URI(url).toURL().openConnection();
            connection.setRequestProperty("Accept-Charset", UTF_8.name());
            connection.setRequestProperty("Cookie", cookies);

            connection.connect();

            String preconnection = String.valueOf(connection.getURL());
            InputStream next = connection.getInputStream();

            String postconnection = String.valueOf(connection.getURL());

            if (!Objects.equals(preconnection, postconnection)) {
                return responseBody;
            } else {
                Scanner nextScanner = new Scanner(next);
                String nextBody = nextScanner.useDelimiter("\\A").next();

                if (nextBody.contains("No posts found.")) {
                    return responseBody;
                } else {
                    responseBody += nextBody;
                    pageNum++;
                    return getFullBody(responseBody, staffID);
                }
            }
        }
        return responseBody;
    }
}
