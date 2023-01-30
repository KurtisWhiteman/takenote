package com.takenote.model;

import com.takenote.helper.ListHelper;
import com.takenote.model.util.TokenType;
import com.takenote.model.util.VerificationToken;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@Entity
public class User extends BaseEntity<Long> {
    @Column(nullable = false, length = 255)
    String name;

    @Column(nullable = false)
    String password;

    @Column(nullable = false, unique = true, length = 255)
    String email;

    @Enumerated(EnumType.STRING)
    UserStatus status;

    @OneToMany(targetEntity = VerificationToken.class, fetch = FetchType.LAZY,
            mappedBy = "user"
    )
    private List<VerificationToken> verificationTokens;

    public VerificationToken getVerificationToken(TokenType tokenType) {
        List<VerificationToken> verificationTokens = this.verificationTokens.stream()
                .filter(verificationToken -> verificationToken.getTokenType().equals(tokenType))
                .collect(Collectors.toList());

        if (!verificationTokens.isEmpty()) {
            return ListHelper.getFirst(verificationTokens);
        }
        return null;
    }
}
