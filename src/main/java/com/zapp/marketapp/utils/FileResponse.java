package com.zapp.marketapp.utils;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileResponse {

    private boolean ok;
    private String file;
    private String file_type;
    private String location;
}
