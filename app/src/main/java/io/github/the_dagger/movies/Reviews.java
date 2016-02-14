package io.github.the_dagger.movies;

import java.util.List;

/**
 * Created by Harshit on 2/15/2016.
 */
public class Reviews {
    public List<SingleReview> reviews;
    public List<SingleReview> getReviews(){
        return reviews;
    }
    public void setReviews(List<SingleReview> reviews) {
        this.reviews = reviews;
    }
    public class SingleReview{
        String content;
        public String getContent(){
            return content;
        }
        public void setContent(String content){
            this.content = content;
        }
    }
}
