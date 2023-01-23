using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class BackgroundMusicManager : MonoBehaviour
{
    void Update()
    {
        if (PlayerPrefs.GetInt("BackgroundMusicToggle") == 0)
        {
            transform.gameObject.GetComponent<AudioSource>().mute = false;
        } else
        {
            transform.gameObject.GetComponent<AudioSource>().mute = true;
        }
    }
}
