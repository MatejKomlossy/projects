using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class WrongDirection : MonoBehaviour
{
    public GameObject wrongDirection;

    private const float MinAngles = 85;
    private const float MaxAngles = 275;

    // Update is called once per frame
    void Update()
    {
        Quaternion headRotation = Camera.main.transform.rotation;
        Vector3 currentEulerAngles = headRotation.eulerAngles;

        if (currentEulerAngles.y > MinAngles && currentEulerAngles.y < MaxAngles)
        {
            wrongDirection.SetActive(true);
            FreezeUnfreezeTime(true);
        }
        else
        {
            wrongDirection.SetActive(false);
            FreezeUnfreezeTime(false);
        }
    }

    private void FreezeUnfreezeTime(bool freeze)
    {
        if (freeze)
        {
            Time.timeScale = 0;
        }
        else
        {
            Time.timeScale = 1;
        }
    }
}
