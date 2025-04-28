package com.example.reviewService.model;

import com.example.reviewService.ReviewServiceApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "reviews")
public class Review {

    //attributes

    @Id
    private String id;

    @Indexed
    private String workerId;

    @Indexed
    private String userId;

    private int rating;

    private String comment;
    private boolean isAnonymous;

    private int helpfulVotes;
    private List<String> voterIds;


    public Review() {}

    private Review (Builder builder) {
        this.id = builder.id;
        this.workerId = builder.workerId;
        this.userId =  builder.userId;
        this.rating= builder.rating;
        this.comment=builder.comment;
        this.isAnonymous= builder.isAnonymous;
        this.helpfulVotes=builder.helpfulVotes;
        this.voterIds=builder.voterIds;
    }

    // equals(), hashCode(), toString()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(id, review.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Review{" +
                "id='" + id + '\'' +
                ", workerId='" + workerId + '\'' +
                ", rating=" + rating +
                ", isAnonymous=" + isAnonymous +
                '}';
    }

    public static class Builder {
        private String id;
        private String workerId;
        private String userId;
        private int rating;
        private String comment = "";
        private boolean isAnonymous = false;
        private int helpfulVotes = 0;
        private List<String> voterIds = new ArrayList<>();

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder workerId(String workerId) {
            this.workerId = workerId;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder rating(int rating) {
            this.rating = rating;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder isAnonymous(boolean isAnonymous) {
            this.isAnonymous = isAnonymous;
            return this;
        }

        public Builder helpfulVotes(int helpfulVotes) {
            this.helpfulVotes = helpfulVotes;
            return this;
        }

        public Builder voterIds(List<String> voterIds) {
            this.voterIds = voterIds;
            return this;

        }
        public Review build() {
            return new Review(this);
        }


    }
    public static class demo {
        public static void main(String[] args) {
            Review review = new Review.Builder()
                    .id("hdbf")
                    .workerId("64654")
                    .userId("34")
                    .isAnonymous(true)
                    .helpfulVotes(4)
                    .build();
            System.out.println(review);

        }
    }

}




