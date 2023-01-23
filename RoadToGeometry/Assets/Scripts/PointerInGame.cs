using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class PointerInGame : MonoBehaviour
{

    private GameObject objectInSight = null;
    private Color normalColor = Color.black;
    private Color activeColor = new Color(0.66f, 0.29f, 0.52f, 1);

    private const string backToMenuName = "MenuPanel";
    private const string playName = "PlayPanel";
    private List<string> clickableNames = new List<string> { backToMenuName, playName };

    // Update is called once per frame
    void Update()
    {
        RaycastHit hit;

        if (Physics.Raycast(Camera.main.transform.position, Camera.main.transform.forward, out hit, Mathf.Infinity))
        {      
            transform.position = hit.point;

            if (objectInSight != hit.transform.gameObject)
            {
                if (objectInSight != null)
                {
                    ChangeObjectColor(normalColor);
                }
                objectInSight = hit.transform.gameObject;
                ChangeObjectColor(activeColor);
            }
        }
        else
        {
            ChangeObjectColor(normalColor);
            objectInSight = null;
        }

        if (Input.GetMouseButtonDown(0))
        {
            switch (objectInSight.name)
            {
                case backToMenuName:
                    BackToMenu();
                    break;
                case playName:
                    Play();
                    break;

                default:
                    break;

            }
        }
    }

    private bool IsClickable(GameObject obj)
    {
        return clickableNames.Contains(obj.name);
    }

    private void ChangeObjectColor(Color color)
    {

        if (IsClickable(objectInSight)) 
        {
            Image img = null;
            if (objectInSight.TryGetComponent<Image>(out img))
            {
                img.color = color;
            }
        }
    }

    public void BackToMenu()
    {
        SceneManager.LoadScene("MainMenu2");
    }

    public void Play()
    {
        SceneManager.LoadScene(SceneManager.GetActiveScene().name);
    }
}
