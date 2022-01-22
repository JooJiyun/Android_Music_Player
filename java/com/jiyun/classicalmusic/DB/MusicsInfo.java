package com.jiyun.classicalmusic.DB;

import com.jiyun.classicalmusic.R;

public class MusicsInfo {
    private  static MusicsInfo _instance = new MusicsInfo();
    public MusicsInfo() {}

    // region Music titles
    public static String[] Titles = {
            "Aria from Bach's Orchestra Suite No. 3",
            "Chopin Etude op.10 No.1",
            "Chopin Etude op.10 No. 5",
            "Chopin Etude Op.10 No. 12",
            "Chopin Etude op.25 No. 9",
            "Liszt Paganini's Grand Practice 'La Campanella'",
            "Spring 1st movement from Vivaldi Four Seasons",
            "Summer 3rd movement from Vivaldi Four Seasons",
            "Winter 1st movement from Vivaldi Four Seasons",
            "Schubert Arpeggione Sonata",
            "Chrysler Joy of Love",
            "Sarasate Chigoinerweizen",
            "Beethoven Violin Sonata No. 5 'Spring' 1st movement",
            "Gershwin Rhapsody in Blue",
            "'Jupiter' from Holst Planet",
            "Mozart Serenade No. 13 ",
            "Bach Invention No. 1",
            "Bach Invention No. 4",
            "Bach Invention No. 8",
            "Bach Invention No. 12",
            "Dvorak Humoresque",
            "Elgar Majesty March 1st",
            "Sativa No. 1 of 3 Gymnopedies",
            "Mendelssohn's Something 30 'Song of Spring'",
            "Schubert Piano Quintet 'Trout' 4th movement",
            "Bach Unaccompanied Cello Suite No. 1 Prelude",
            "Russian Dance from Stravinsky Petroska",
            "Label Bolero",
            "Schubert Improvisation D.899",
            "Bach Toccata and Fugue",
            "J. Strauss I Radetzky March",
            "Beethoven Symphony No. 5 'Fate' 1st movement",
            "Chopin Nocturne No. 2",
    };
    // endregion

    // region Music times
    private static String[] Times = {
            "06:30",
            "02:00",
            "01:39",
            "02:42",
            "00:59",
            "04:51",
            "03:39",
            "03:21",
            "03:36",
            "08:36",
            "03:23",
            "08:41",
            "10:12",
            "16:29",
            "08:19",
            "05:39",
            "01:30",
            "00:45",
            "01:03",
            "00:44",
            "03:49",
            "04:24",
            "03:06",
            "02:32",
            "08:21",
            "02:50",
            "02:32",
            "15:02",
            "04:23",
            "09:03",
            "03:30",
            "06:58",
            "04:26",
    };
    // endregion

    // region Music thumbnails
    private static int[] Thumbs = {
            R.drawable.thumb_bach,
            R.drawable.thumb_chopin,
            R.drawable.thumb_chopin,
            R.drawable.thumb_chopin,
            R.drawable.thumb_chopin,
            R.drawable.thumb_paganini,
            R.drawable.thumb_vivaldi,
            R.drawable.thumb_vivaldi,
            R.drawable.thumb_vivaldi,
            R.drawable.thumb_schubert,
            R.drawable.thumb_kreisler,
            R.drawable.thumb_sarasate,
            R.drawable.thumb_beethoven,
            R.drawable.thumb_gershwin,
            R.drawable.thumb_holst,
            R.drawable.thumb_mozart,
            R.drawable.thumb_bach,
            R.drawable.thumb_bach,
            R.drawable.thumb_bach,
            R.drawable.thumb_bach,
            R.drawable.thumb_dvorak,
            R.drawable.thumb_elgar,
            R.drawable.thumb_satie,
            R.drawable.thumb_mendelssohn,
            R.drawable.thumb_schubert,
            R.drawable.thumb_bach,
            R.drawable.thumb_stravinsky,
            R.drawable.thumb_ravel,
            R.drawable.thumb_schubert,
            R.drawable.thumb_bach,
            R.drawable.thumb_strauss,
            R.drawable.thumb_beethoven,
            R.drawable.thumb_chopin,
    };
    // endregion

    // region Music files
    private static int[] Musics = {
            R.raw.music1,
            R.raw.music2,
            R.raw.music3,
            R.raw.music4,
            R.raw.music5,
            R.raw.music6,
            R.raw.music7,
            R.raw.music8,
            R.raw.music9,
            R.raw.music11,
            R.raw.music12,
            R.raw.music13,
            R.raw.music14,
            R.raw.music15,
            R.raw.music16,
            R.raw.music17,
            R.raw.music18,
            R.raw.music19,
            R.raw.music20,
            R.raw.music21,
            R.raw.music22,
            R.raw.music23,
            R.raw.music24,
            R.raw.music25,
            R.raw.music26,
            R.raw.music27,
            R.raw.music28,
            R.raw.music29,
            R.raw.music30,
            R.raw.music31,
            R.raw.music32,
            R.raw.music33,
            R.raw.music34,
    };
    // endregion


    public static String getTitle(int idx){
        return Titles[idx];
    }
    public static String getTime(int idx){
        return Times[idx];
    }
    public static int getImg(int idx){
        return Thumbs[idx];
    }
    public static int getMusic(int idx){
        return Musics[idx];
    }

}
